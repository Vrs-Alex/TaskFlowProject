package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.data.local.db.datasource.sync.PendingOperationLocalDataSource
import com.vrsalex.taskflow.data.local.db.entity.sync.PendingOperationEntity
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Класс для обработки операций из очереди Outbox
 * Нужен для синхронизации данных между клиентом и сервером
 */
class OutboxHandler(
    private val pendingOperationLocalDataSource: PendingOperationLocalDataSource
) {

    private val handlers = mutableMapOf<SyncDbEntity, OutboxEntityHandler>()

    fun register(entity: SyncDbEntity, handler: OutboxEntityHandler) {
        handlers[entity] = handler
    }

    suspend fun addOperation(itemId: Uuid, entityType: SyncDbEntity, operation: PendingOperation) {
        val existing = pendingOperationLocalDataSource.findByItemId(itemId)
        val collapsed = collapse(existing?.operation, operation)

        if (collapsed == null) {
            pendingOperationLocalDataSource.delete(itemId)
        } else {
            pendingOperationLocalDataSource.insert(
                PendingOperationEntity(
                    itemId = itemId,
                    entityType = entityType,
                    operation = collapsed,
                    createdAt = existing?.createdAt ?: Clock.System.now()
                )
            )
        }
        process()
    }

    private fun collapse(existing: PendingOperation?, new: PendingOperation): PendingOperation? =
        when {
            existing == null -> new
            existing == PendingOperation.CREATE && new == PendingOperation.UPDATE -> PendingOperation.CREATE
            existing == PendingOperation.CREATE && new == PendingOperation.DELETE -> null
            existing == PendingOperation.UPDATE && new == PendingOperation.DELETE -> PendingOperation.DELETE
            existing == PendingOperation.DELETE -> PendingOperation.DELETE
            else -> new
        }

    suspend fun process() {
        val pending = pendingOperationLocalDataSource.getAll()
            .sortedBy { it.entityType.priority }

        for (operation in pending) {
            val handler = handlers[operation.entityType] ?: continue

            val result = when (operation.operation) {
                PendingOperation.CREATE -> handler.create(operation.itemId)
                PendingOperation.UPDATE -> handler.update(operation.itemId)
                PendingOperation.DELETE -> handler.delete(operation.itemId)
            }


            when (result) {
                is Resource.Success -> {
                    if (operation.operation != PendingOperation.DELETE) {
                        val syncModel = (result as Resource.Success<SyncModel>).data
                        handler.markAsSynced(operation.itemId, syncModel)
                    }
                    pendingOperationLocalDataSource.delete(operation.itemId)
                }
                is Resource.Conflict -> {
                    val existing = handler.findExisting(operation.itemId)
                    if (existing != null) {
                        handler.markAsSynced(operation.itemId, existing)
                        pendingOperationLocalDataSource.delete(operation.itemId)
                    } else break
                }
                is Resource.Error -> break
            }
        }
    }
}