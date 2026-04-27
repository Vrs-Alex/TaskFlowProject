package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.data.local.db.datasource.PendingOperationLocalDataSource
import com.vrsalex.taskflow.data.local.db.entity.PendingOperationEntity
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import kotlin.uuid.Uuid

class OutboxHandler(
    private val pendingOperationLocalDataSource: PendingOperationLocalDataSource
) {
    private val handlers = mutableMapOf<SyncDbEntity, OutboxEntityHandler>()

    fun register(entity: SyncDbEntity, handler: OutboxEntityHandler) {
        handlers[entity] = handler
    }

    suspend fun addOperation(itemId: Uuid, entityType: SyncDbEntity, operation: PendingOperation) {
        pendingOperationLocalDataSource.insert(
            PendingOperationEntity(
                itemId = itemId,
                entityType = entityType,
                operation = operation
            )
        )
        process()
    }

    suspend fun process() {
        val pending = pendingOperationLocalDataSource.getAll()
        pending.forEach { operation ->
            val handler = handlers[operation.entityType] ?: return@forEach
            val result = when (operation.operation) {
                PendingOperation.CREATE -> handler.create(operation.itemId)
                PendingOperation.UPDATE -> handler.update(operation.itemId)
                PendingOperation.DELETE -> handler.delete(operation.itemId)
            }
            if (result is Resource.Success) {
                pendingOperationLocalDataSource.delete(operation.itemId)
            }
        }
    }
}