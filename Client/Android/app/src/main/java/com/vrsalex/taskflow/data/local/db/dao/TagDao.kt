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
    @Query("SELECT * FROM tag")
    fun getTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getById(id: Uuid): TagEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: TagEntity): Long

    @Update
    suspend fun update(event: TagEntity)

    suspend fun upsert(tag: TagEntity) {
        if (insert(tag) == -1L) update(tag)
    }

    @Query("DELETE FROM tag WHERE id = :tagId")
    suspend fun delete(tagId: Uuid)
}