package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import kotlin.uuid.Uuid

@Dao
interface ItemTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: ItemTagCrossRef)

    @Query("DELETE FROM item_tag WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: Uuid)

    @Query("DELETE FROM item_tag WHERE itemId = :itemId AND tagId = :tagId")
    suspend fun delete(itemId: Uuid, tagId: Uuid)
}