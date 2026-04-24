package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItem
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM item WHERE type = 'EVENT'")
    fun getEventsWithItems(): Flow<List<EventWithItem>>

    @Query(
        """
        SELECT * 
        FROM item
        INNER JOIN event ON item.id = event.itemId
        WHERE item.type = 'EVENT'
          AND event.startDate <= :date
          AND event.endDate >= :date
        """
    )
    fun getEventsWithItems(date: Instant): Flow<List<EventWithItem>>

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id AND type = 'EVENT'")
    suspend fun getEventById(id: Uuid): EventWithItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Query("DELETE FROM event WHERE itemId = :itemId")
    suspend fun delete(itemId: Uuid)
}