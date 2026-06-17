package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.data.local.db.dao.SyncCursorDao
import com.vrsalex.taskflow.data.local.db.entity.SyncCursorEntity
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.sync.repository.SyncCursorStore
import kotlin.time.Instant

class SyncCursorStoreImpl(
    private val dao: SyncCursorDao,
) : SyncCursorStore {

    override suspend fun getLastSync(entity: SyncEntity): Instant? = dao.getLastSync(entity)

    override suspend fun setLastSync(entity: SyncEntity, value: Instant) =
        dao.upsert(SyncCursorEntity(entity = entity, lastSync = value))
}
