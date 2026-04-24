package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.relation.ItemWithTagsAndArea
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ItemDao {

    @Transaction
    @Query("SELECT * FROM item")
    fun getItems(): Flow<List<ItemWithTagsAndArea>>

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Uuid): Flow<ItemWithTagsAndArea?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    suspend fun upsert(item: ItemEntity) {
        if (insert(item) == -1L) update(item)
    }

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Uuid)

}