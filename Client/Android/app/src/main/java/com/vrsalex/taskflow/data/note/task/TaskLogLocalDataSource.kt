package com.vrsalex.taskflow.data.note.task

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.domain.note.task.TaskLogCreate
import kotlinx.coroutines.flow.Flow
import vrsalex.shared.api.item.task.TaskLogDto
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.uuid.Uuid

class TaskLogLocalDataSource(private val db: AppDatabase) {

    private val dao get() = db.taskLogDao()

    fun observeByTask(taskId: Uuid): Flow<List<TaskLogEntity>> = dao.observeByTask(taskId)
    suspend fun getRaw(id: Uuid) = dao.getRaw(id)

    suspend fun create(data: TaskLogCreate) = dao.upsert(data.toEntity())

    suspend fun upsertFromRemote(dto: TaskLogDto) = dao.upsert(dto.toEntity())
    suspend fun softDelete(id: Uuid) = dao.softDelete(id)
    suspend fun delete(id: Uuid) = dao.delete(id)


    suspend fun setDone(taskId: Uuid, date: LocalDate, done: Boolean) = db.withTransaction {
        val existing = dao.getByTaskAndDate(taskId, date)
        if (done) {
            val now = Clock.System.now()
            val log = existing?.copy(
                completedAt = now,
                sync = existing.sync.copy(isDeleted = false, isSynced = false, updatedAt = now)
            ) ?: TaskLogEntity(
                id = Uuid.random(),
                taskId = taskId,
                date = date,
                completedAt = now,
                sync = newLocalSync(now),
            )
            dao.upsert(log)
        } else {
            existing?.let { dao.softDelete(it.id) }
        }
    }
}
