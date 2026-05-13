package vrsalex.task.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.TaskLogs
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.task.domain.TaskLog
import vrsalex.task.domain.TaskLogCreate
import vrsalex.task.domain.TaskLogRepository
import vrsalex.task.domain.TaskLogUpdate
import kotlin.time.Clock

class TaskLogR2dbcRepository : TaskLogRepository,
    BaseSyncRepository<TaskLog, TaskLogCreate, TaskLogUpdate, TaskLogs>(TaskLogs) {

    override suspend fun ResultRow.toDomain(): TaskLog = TaskLog(
        id = this[TaskLogs.id].value,
        userId = this[TaskLogs.userId].value,
        clientId = this[TaskLogs.clientId],
        version = this[TaskLogs.version],
        updatedAt = this[TaskLogs.updatedAt],
        isDeleted = this[TaskLogs.isDeleted],
        createdAt = this[TaskLogs.createdAt],
        taskId = this[TaskLogs.task]?.value,
        clientTaskId = this[TaskLogs.clientTaskId],
        date = this[TaskLogs.date],
        completedAt = this[TaskLogs.completedAt] ?: this[TaskLogs.createdAt]
    )

    override suspend fun create(data: TaskLogCreate, _userId: Long): TaskLog =
        safeQuery("Не удалось создать запись задачи", logger) {
            val id = TaskLogs.insertAndGetId {
                it[TaskLogs.userId] = _userId
                it[TaskLogs.clientId] = data.clientId
                it[TaskLogs.task] = data.taskId
                it[TaskLogs.clientTaskId] = data.clientTaskId
                it[TaskLogs.date] = data.date
                it[TaskLogs.completedAt] = data.completedAt
            }.value
            findById(id, _userId) ?: throw AppException.BadRequest("Не удалось создать запись задачи")
        }

    override suspend fun update(data: TaskLogUpdate, userId: Long): TaskLog =
        safeQuery("Не удалось обновить запись задачи", logger) {
            val updatedRows = TaskLogs.update({
                (TaskLogs.id eq data.id) and (TaskLogs.clientId eq data.clientId) and
                        (TaskLogs.userId eq userId) and (TaskLogs.version eq data.version)
            }) { statement ->
                statement[TaskLogs.version] = data.version + 1
                statement[TaskLogs.updatedAt] = Clock.System.now()
            }
            checkUpdateResult(updatedRows, data.id, userId, "Записи задачи")
            findById(data.id, userId) ?: throw AppException.BadRequest("Не удалось обновить запись задачи")
        }
}
