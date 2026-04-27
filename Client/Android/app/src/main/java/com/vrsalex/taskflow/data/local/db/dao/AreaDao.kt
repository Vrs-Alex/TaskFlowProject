package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface AreaDao {
    @Transaction
    @Query("SELECT * FROM area WHERE isDeleted = 0")
    fun getAreas(): Flow<List<AreaEntity>>

    @Query("SELECT * FROM area WHERE id = :id AND isDeleted = 0")
    fun getArea(id: Uuid): Flow<AreaEntity?>

    @Query("SELECT * FROM area WHERE id = :id")
    suspend fun getByIdRaw(id: Uuid): AreaEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(area: AreaEntity): Long

    @Update
    suspend fun update(area: AreaEntity)

    suspend fun upsert(area: AreaEntity) {
        if (insert(area) == -1L) update(area)
    }

    @Query("""
        UPDATE area 
        SET isSynced = 1, 
            serverId = :serverId, 
            version = :version, 
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun markSynced(id: Uuid, serverId: Long, version: Int, updatedAt: Instant)

    @Query("DELETE FROM area WHERE id = :id")
    suspend fun delete(id: Uuid)

    @Query("UPDATE area SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)
}