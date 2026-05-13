package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.data.local.db.datasource.sync.PendingOperationLocalDataSource
import com.vrsalex.taskflow.data.local.db.entity.sync.PendingOperationEntity
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.uuid.Uuid

@OptIn(FlowPreview::class)
class OutboxHandler(
    private val coroutineScope: CoroutineScope,
    private val pendingOperationLocalDataSource: PendingOperationLocalDataSource
) {

    private val handlers = mutableMapOf<SyncDbEntity, OutboxEntityHandler>()

    fun register(entity: SyncDbEntity, handler: OutboxEntityHandler) {
        handlers[entity] = handler
    }

    private val writeMutex = Mutex()
    private val processMutex = Mutex()

    private val processTrigger = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        coroutineScope.launch {
            processTrigger
                .debounce(350)
                .collect { process() }
        }
    }

    fun cancelOperation(id: Uuid) = coroutineScope.launch(Dispatchers.IO) {
        writeMutex.withLock { pendingOperationLocalDataSource.delete(id) }
    }

    fun addOperation(id: Uuid, entityType: SyncDbEntity, operation: PendingOperation) =
        coroutineScope.launch(Dispatchers.IO) {
            writeMutex.withLock {
                val existing = pendingOperationLocalDataSource.findByItemId(id)
                val collapsed = collapse(existing?.operation, operation)

                if (collapsed == null) {
                    pendingOperationLocalDataSource.delete(id)
                } else {
                    pendingOperationLocalDataSource.insert(
                        PendingOperationEntity(
                            id = id,
                            entityType = entityType,
                            operation = collapsed,
                            createdAt = existing?.createdAt ?: Clock.System.now()
                        )
                    )
                }
            }
            processTrigger.emit(Unit)
        }

    private fun collapse(existing: PendingOperation?, new: PendingOperation): PendingOperation? =
        when {
            existing == null -> new
            existing == PendingOperation.CREATE && new == PendingOperation.UPDATE -> PendingOperation.CREATE
            existing == PendingOperation.CREATE && new == PendingOperation.DELETE -> null
            existing == PendingOperation.UPDATE && new == PendingOperation.DELETE -> PendingOperation.DELETE
            existing == PendingOperation.DELETE && new == PendingOperation.CREATE -> null
            existing == PendingOperation.DELETE -> PendingOperation.DELETE
            else -> new
        }

    suspend fun process() {
        if (!processMutex.tryLock()) return
        try {
            val pending = pendingOperationLocalDataSource.getAll()
                .sortedWith(compareBy({ it.entityType.priority }, { it.createdAt }))

            var blockedFromPriority = Int.MAX_VALUE

            for (operation in pending) {
                if (operation.entityType.priority > blockedFromPriority) continue

                val handler = handlers[operation.entityType] ?: continue

                val result = when (operation.operation) {
                    PendingOperation.CREATE -> handler.create(operation.id)
                    PendingOperation.UPDATE -> handler.update(operation.id)
                    PendingOperation.DELETE -> handler.delete(operation.id)
                }

                when (result) {
                    is Resource.Success -> {
                        if (operation.operation != PendingOperation.DELETE) {
                            val syncModel = (result as Resource.Success<SyncModel>).data
                            handler.markAsSynced(operation.id, syncModel)
                        }
                        pendingOperationLocalDataSource.delete(operation.id)
                    }
                    is Resource.Failure.Conflict -> {
                        val existing = handler.findExisting(operation.id)
                        if (existing != null) {
                            handler.markAsSynced(operation.id, existing)
                            pendingOperationLocalDataSource.delete(operation.id)
                        } else {
                            handleFailure(operation)
                            blockedFromPriority = minOf(blockedFromPriority, operation.entityType.priority)
                        }
                    }
                    is Resource.Failure.Unavailable -> {
                        blockedFromPriority = minOf(blockedFromPriority, operation.entityType.priority)
                        scheduleRetry()
                    }
                    is Resource.Failure.Unauthorized -> {
                        blockedFromPriority = 0
                    }
                    is Resource.Failure.Error -> {
                        handleFailure(operation)
                        blockedFromPriority = minOf(blockedFromPriority, operation.entityType.priority)
                        scheduleRetry()
                    }
                }
            }
        } finally {
            processMutex.unlock()
        }
    }

    private fun scheduleRetry() {
        coroutineScope.launch {
            delay(RETRY_DELAY_MS)
            processTrigger.emit(Unit)
        }
    }

    private suspend fun handleFailure(operation: PendingOperationEntity) {
        if (operation.retryCount >= MAX_RETRIES) {
            pendingOperationLocalDataSource.delete(operation.id)
        } else {
            pendingOperationLocalDataSource.incrementRetryCount(operation.id)
        }
    }

    companion object {
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MS = 30_000L
    }
}
