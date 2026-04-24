package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.data.local.db.dao.SyncDao
import com.vrsalex.taskflow.data.local.db.entity.SyncEntity
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlin.time.Instant

class SyncRepositoryImpl(
    private val syncDao: SyncDao
): SyncRepository {

    override suspend fun getLastSync(entity: SyncDbEntity): Instant? =
        syncDao.getLastSyncedAt(entity)

    override suspend fun setLastSync(
        entity: SyncDbEntity,
        lastSync: Instant
    ) = syncDao.updateLastSyncedAt(SyncEntity(entity.name, lastSync))
}