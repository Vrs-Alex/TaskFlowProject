package com.vrsalex.taskflow.data.local.db.dao.item

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface TaskLogDao {

    // Search

    @Query("SELECT * FROM task_log WHERE id = :id")
    suspend fun getByIdRaw(id: Uuid): TaskLogEntity?

    @Transaction
    @Query("SELECT * FROM task_log WHERE id = :id AND isDeleted = 0")
    fun getLog(id: Uuid): Flow<TaskLogEntity?>

    @Transaction
    @Query("SELECT * FROM task_log WHERE isDeleted = 0 AND taskId = :taskId")
    fun getByTaskId(taskId: Uuid): Flow<List<TaskLogEntity>>

    @Query("SELECT * FROM task_log WHERE taskId = :taskId AND date = :date AND isDeleted = 0 LIMIT 1")
    suspend fun getByTaskAndDate(taskId: Uuid, date: LocalDate): TaskLogEntity?

    // Insert, Update, Delete

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TaskLogEntity): Long

    @Update
    suspend fun update(item: TaskLogEntity)

    suspend fun upsert(item: TaskLogEntity) {
        if (insert(item) == -1L) update(item)
    }

    @Query("DELETE FROM task_log WHERE id = :id")
    suspend fun delete(id: Uuid)

    @Query("UPDATE task_log SET isDeleted = 0, isSynced = 0 WHERE id = :id")
    suspend fun undelete(id: Uuid)

    // Sync operations

    @Query("""
        UPDATE task_log 
        SET isSynced = 1,
            id = :newId,
            serverId = :serverId, 
            version = :version, 
            updatedAt = :updatedAt
        WHERE id = :id
    """)
    suspend fun markSynced(id: Uuid, newId: Uuid, serverId: Long, version: Int, updatedAt: Instant)

    @Query("UPDATE task_log SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)


}