package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.SyncCursorEntity
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlin.time.Instant

@Dao
interface SyncCursorDao {

    @Query("SELECT lastSync FROM sync_cursor WHERE entity = :entity")
    suspend fun getLastSync(entity: SyncEntity): Instant?

    @Upsert
    suspend fun upsert(cursor: SyncCursorEntity)
}
