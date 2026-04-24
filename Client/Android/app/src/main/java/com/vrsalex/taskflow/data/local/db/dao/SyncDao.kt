package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vrsalex.taskflow.data.local.db.entity.SyncEntity
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.time.Instant

@Dao
interface SyncDao {

    @Query("SELECT lastSyncedAt FROM sync WHERE entity = :entity")
    suspend fun getLastSyncedAt(entity: SyncDbEntity): Instant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastSyncedAt(entity: SyncEntity)

}