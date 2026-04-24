package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItem
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface TagDao {

    @Transaction
    @Query("SELECT * FROM tag")
    fun getTags(): Flow<List<TagEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: TagEntity)

    @Update
    suspend fun update(event: TagEntity)

    @Query("DELETE FROM tag WHERE id = :tagId")
    suspend fun delete(tagId: Uuid)
}