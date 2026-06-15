package com.vrsalex.taskflow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@Dao
interface TaskLogDao {

    @Upsert suspend fun upsert(log: TaskLogEntity)

    @Query("SELECT * FROM task_log WHERE id = :id")
    suspend fun getRaw(id: Uuid): TaskLogEntity?

    @Query("SELECT * FROM task_log WHERE taskId = :taskId AND date = :date LIMIT 1")
    suspend fun getByTaskAndDate(taskId: Uuid, date: LocalDate): TaskLogEntity?

    @Query("SELECT * FROM task_log WHERE taskId = :taskId AND isDeleted = 0")
    fun observeByTask(taskId: Uuid): Flow<List<TaskLogEntity>>

    @Query("UPDATE task_log SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun softDelete(id: Uuid)

    @Query("DELETE FROM task_log WHERE id = :id")
    suspend fun delete(id: Uuid)
}
