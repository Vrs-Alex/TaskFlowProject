package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface AreaDao {

    @Upsert suspend fun upsert(area: AreaEntity)

    @Query("SELECT * FROM area WHERE id = :id")
    suspend fun getRaw(id: Uuid): AreaEntity?

    @Query("SELECT * FROM area WHERE id = :id AND isDeleted = 0")
    fun observe(id: Uuid): Flow<AreaEntity?>

    @Query("SELECT * FROM area WHERE isDeleted = 0 ORDER BY name ASC")
    fun observeAll(): Flow<List<AreaEntity>>

    @Query("UPDATE area SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM area WHERE id = :id")
    suspend fun delete(id: Uuid)
}
