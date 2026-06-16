package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@Dao
interface TaskDao {

    @Upsert suspend fun upsert(task: TaskEntity)

    @Query("SELECT note.* FROM note INNER JOIN task ON note.id = task.id WHERE note.id = :id")
    @Transaction
    suspend fun getRaw(id: Uuid): TaskRelation?

    @Transaction
    @Query("SELECT note.* FROM note INNER JOIN task ON note.id = task.id WHERE note.id = :id AND note.isDeleted = 0")
    fun observe(id: Uuid): Flow<TaskRelation?>

    @Transaction
    @Query("SELECT note.* FROM note INNER JOIN task ON note.id = task.id WHERE note.isDeleted = 0")
    fun observeAll(): Flow<List<TaskRelation>>

    /** Inbox: задачи без даты и без области. */
    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN task ON note.id = task.id
        WHERE note.isDeleted = 0 AND task.dueDate IS NULL AND note.areaId IS NULL
        ORDER BY note.updatedAt DESC
    """)
    fun observeInbox(): Flow<List<TaskRelation>>

    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN task ON note.id = task.id
        WHERE note.isDeleted = 0 AND (
            task.dueDate = :date
            OR (
                task.recurrenceType IS NOT NULL
                AND task.dueDate <= :date
                AND (task.recurrenceEndDate IS NULL OR task.recurrenceEndDate >= :date)
            )
        )
        ORDER BY task.dueTime ASC
    """)
    fun observeByDate(date: LocalDate): Flow<List<TaskRelation>>

    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN task ON note.id = task.id
        WHERE note.isDeleted = 0 AND (
            (task.recurrenceType IS NULL AND task.dueDate BETWEEN :from AND :to)
            OR (task.recurrenceType IS NOT NULL AND task.dueDate <= :to
                AND (task.recurrenceEndDate IS NULL OR task.recurrenceEndDate >= :from))
        )
    """)
    fun observeInRange(from: LocalDate, to: LocalDate): Flow<List<TaskRelation>>

    @Transaction
    @Query("""
        SELECT note.* FROM note
        INNER JOIN task ON note.id = task.id
        WHERE note.isDeleted = 0 AND task.dueDate < :today AND (
            task.recurrenceType IS NOT NULL
            OR NOT EXISTS (
                SELECT 1 FROM task_log
                WHERE task_log.taskId = task.id
                AND task_log.isDeleted = 0
                AND task_log.date = task.dueDate
            )
        )
        ORDER BY task.dueDate ASC
    """)
    fun observeOverdueCandidates(today: LocalDate): Flow<List<TaskRelation>>

    @Query("UPDATE note SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: Uuid)
}
