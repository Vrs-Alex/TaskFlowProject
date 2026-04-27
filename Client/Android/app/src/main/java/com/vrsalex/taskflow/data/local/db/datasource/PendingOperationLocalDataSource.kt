package com.vrsalex.taskflow.data.local.db.datasource

import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.PendingOperationEntity
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.uuid.Uuid

class PendingOperationLocalDataSource(
    private val db: AppDatabase
) {
    suspend fun insert(operation: PendingOperationEntity) =
        db.pendingOperationDao().insert(operation)

    suspend fun getAll(): List<PendingOperationEntity> =
        db.pendingOperationDao().getAll()

    suspend fun delete(itemId: Uuid) =
        db.pendingOperationDao().delete(itemId)

    suspend fun getByEntityType(entityType: SyncDbEntity) =
        db.pendingOperationDao().getByEntityType(entityType)
}