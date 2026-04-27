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
        WHERE isDeleted = 0
    """)
    fun getEvents(): Flow<List<EventWithItemTagsAndArea>>

    @Transaction
    @Query("""
        SELECT item.* FROM item
        INNER JOIN event ON item.id = event.itemId
        WHERE event.startDate <= :endOfDay
        AND event.endDate >= :startOfDay 
        AND isDeleted = 0
    """)
    fun getEvents(startOfDay: Instant, endOfDay: Instant): Flow<List<EventWithItemTagsAndArea>>


    @Transaction
    @Query("""
        SELECT item.* FROM item 
        WHERE item.id = :id AND isDeleted = 0
    """)
    fun getEvent(id: Uuid): Flow<EventWithItemTagsAndArea?>

    @Transaction
    @Query("SELECT item.* FROM item INNER JOIN event ON item.id = event.itemId WHERE item.id = :id")
    suspend fun getEventByIdRaw(id: Uuid): EventWithItemTagsAndArea?  // без фильтра isDeleted

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