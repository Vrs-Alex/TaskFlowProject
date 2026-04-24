package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface ItemDao {

    @Query("SELECT * FROM item WHERE id = :id")
    suspend fun getById(id: Uuid): ItemEntity?

    @Query("SELECT * FROM item WHERE serverId = :id")
    suspend fun getServerId(id: Long): ItemEntity?

    @Query("SELECT * FROM item")
    fun getAll(): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Uuid)
}