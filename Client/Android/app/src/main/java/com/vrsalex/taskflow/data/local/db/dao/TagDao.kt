package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface TagDao {
    @Transaction
    @Query("SELECT * FROM tag WHERE isDeleted = 0")
    fun getTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag WHERE id = :id AND isDeleted = 0")
    fun getTag(id: Uuid): Flow<TagEntity?>

    @Query("SELECT * FROM tag WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: Uuid): TagEntity?

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getByIdRaw(id: Uuid): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: TagEntity): Long

    @Update
    suspend fun update(tag: TagEntity)

    suspend fun upsert(tag: TagEntity) {
        if (insert(tag) == -1L) update(tag)
    }

    @Query("DELETE FROM tag WHERE id = :id")
    suspend fun delete(id: Uuid)

    @Query("UPDATE tag SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)
}