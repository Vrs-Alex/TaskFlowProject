package com.vrsalex.taskflow.data.local.db.dao.item

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.item.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface EventDao {

    // Search

    @Transaction
    @Query("""
        SELECT item.* FROM item 
        INNER JOIN event ON item.id = event.itemId
        WHERE isDeleted = 0
    """)
    fun getEvents(): Flow<List<EventRelation>>

    @Transaction
    @Query("""
        SELECT item.* FROM item
        INNER JOIN event ON item.id = event.itemId
        WHERE event.startDate <= :endOfDay
        AND event.endDate >= :startOfDay 
        AND isDeleted = 0
        AND (:withArchived = 1 OR status != 'ARCHIVED')
        ORDER BY event.endDate DESC
    """)
    fun getEvents(startOfDay: Instant, endOfDay: Instant, withArchived: Boolean = false): Flow<List<EventRelation>>


    @Transaction
    @Query("""
        SELECT item.* FROM item
        INNER JOIN event ON item.id = event.itemId
        WHERE status = 'ARCHIVED' AND isDeleted = 0
        AND (:query = '' OR item.name LIKE '%' || :query || '%')
        ORDER BY item.updatedAt DESC
    """)
    fun getArchivedEvents(query: String = ""): Flow<List<EventRelation>>

    @Transaction
    @Query("""
        SELECT item.* FROM item
        WHERE item.id = :id AND isDeleted = 0
    """)
    fun getEvent(id: Uuid): Flow<EventRelation?>

    @Transaction
    @Query("SELECT item.* FROM item INNER JOIN event ON item.id = event.itemId WHERE item.id = :id")
    suspend fun getEventByIdRaw(id: Uuid): EventRelation?  // без фильтра isDeleted


    // Insert, Update, Delete

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