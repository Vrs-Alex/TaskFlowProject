package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItemTagsAndArea
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface EventDao {

    @Transaction
    @Query("""
        SELECT item.* FROM item 
        INNER JOIN event ON item.id = event.itemId
    """)
    fun getEvents(): Flow<List<EventWithItemTagsAndArea>>

    @Transaction
    @Query("""
        SELECT item.* FROM item
        INNER JOIN event ON item.id = event.itemId
        WHERE event.startDate <= :date
        AND event.endDate >= :date
    """)
    fun getEvents(date: Instant): Flow<List<EventWithItemTagsAndArea>>

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id AND type = 'EVENT'")
    suspend fun getEventById(id: Uuid): EventWithItemTagsAndArea?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: EventEntity): Long

    @Update
    suspend fun update(event: EventEntity)

    suspend fun upsert(event: EventEntity) {
        if (insert(event) == -1L) update(event)
    }

    @Query("DELETE FROM event WHERE itemId = :itemId")
    suspend fun delete(itemId: Uuid)
}