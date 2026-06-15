package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TagDao {

    @Upsert suspend fun upsert(tag: TagEntity)

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getRaw(id: Uuid): TagEntity?

    @Query("SELECT * FROM tag WHERE id = :id AND isDeleted = 0")
    fun observe(id: Uuid): Flow<TagEntity?>

    @Query("SELECT * FROM tag WHERE isDeleted = 0 ORDER BY name ASC")
    fun observeAll(): Flow<List<TagEntity>>

    @Query("UPDATE tag SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM tag WHERE id = :id")
    suspend fun delete(id: Uuid)
}
