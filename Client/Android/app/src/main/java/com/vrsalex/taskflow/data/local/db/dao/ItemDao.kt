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
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface ItemDao {
    @Transaction
    @Query("SELECT * FROM item WHERE isDeleted = 0")
    fun getItems(): Flow<List<ItemWithTagsAndArea>>

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id AND isDeleted = 0")
    fun getItem(id: Uuid): Flow<ItemWithTagsAndArea?>

    @Query("SELECT * FROM item WHERE id = :id")
    suspend fun getByIdRaw(id: Uuid): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    suspend fun upsert(item: ItemEntity) {
        if (insert(item) == -1L) update(item)
    }

    @Query("""
        UPDATE item 
        SET isSynced = 1,
            id = :newId,
            serverId = :serverId, 
            version = :version, 
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun markSynced(id: Uuid, newId: Uuid, serverId: Long, version: Int, updatedAt: Instant)


    @Query(
        """
        UPDATE item
            SET areaId = :new
            WHERE areaId = :old
        """)
    suspend fun updateAreaId(old: Uuid, new: Uuid)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Uuid)

    @Query("UPDATE item SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)
}