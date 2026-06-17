package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface EventDao {

    @Upsert suspend fun upsert(event: EventEntity)

    @Query("SELECT note.* FROM note INNER JOIN event ON note.id = event.id WHERE note.id = :id")
    @Transaction
    suspend fun getRaw(id: Uuid): EventRelation?

    @Transaction
    @Query("SELECT note.* FROM note INNER JOIN event ON note.id = event.id WHERE note.isSynced = 0")
    suspend fun getDirty(): List<EventRelation>

    @Transaction
    @Query("SELECT note.* FROM note INNER JOIN event ON note.id = event.id WHERE note.id = :id AND note.isDeleted = 0")
    fun observe(id: Uuid): Flow<EventRelation?>

    @Transaction
    @Query("SELECT note.* FROM note INNER JOIN event ON note.id = event.id WHERE note.isDeleted = 0")
    fun observeAll(): Flow<List<EventRelation>>

    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN event ON note.id = event.id
        WHERE note.isDeleted = 0
          AND event.startDate < :to
          AND (event.endDate IS NULL OR event.endDate >= :from)
        ORDER BY event.startDate ASC
    """)
    fun observeBetween(from: Instant, to: Instant): Flow<List<EventRelation>>

    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN event ON note.id = event.id
        WHERE note.isDeleted = 0 AND note.status = 'ARCHIVE'
          AND (:query = '' OR note.name LIKE '%' || :query || '%')
        ORDER BY note.updatedAt DESC
    """)
    fun observeArchived(query: String): Flow<List<EventRelation>>

    @Query("UPDATE note SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: Uuid)
}
