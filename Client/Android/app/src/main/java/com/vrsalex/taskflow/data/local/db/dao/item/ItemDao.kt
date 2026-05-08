package com.vrsalex.taskflow.data.local.db.dao.item

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.ItemRelation
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface ItemDao {

    // Search

    @Query("SELECT * FROM item WHERE id = :id")
    suspend fun getByIdRaw(id: Uuid): ItemEntity?

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id AND isDeleted = 0")
    fun getItem(id: Uuid): Flow<ItemRelation?>

    @Transaction
    @Query("SELECT * FROM item WHERE isDeleted = 0")
    fun getItems(): Flow<List<ItemRelation>>



    // Insert, Update, Delete

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    suspend fun upsert(item: ItemEntity) {
        if (insert(item) == -1L) update(item)
    }

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Uuid)


    // Sync operations

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

    @Query("UPDATE item SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)


    // Additional operations

    @Query(
        """
        UPDATE item
            SET areaId = :new
            WHERE areaId = :old
        """)
    suspend fun updateAreaId(old: Uuid, new: Uuid)

}