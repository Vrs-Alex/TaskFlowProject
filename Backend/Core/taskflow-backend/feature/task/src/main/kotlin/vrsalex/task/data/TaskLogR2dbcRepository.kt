package vrsalex.task.data

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.update
import vrsalex.core.database.TaskLogTable
import vrsalex.core.database.utils.safeQuery
import vrsalex.core.exception.AppException
import vrsalex.core.sync.repository.BaseSyncRepository
import vrsalex.task.domain.TaskLog
import vrsalex.task.domain.TaskLogCreate
import vrsalex.task.domain.TaskLogRepository
import vrsalex.task.domain.TaskLogUpdate
import kotlin.time.Clock

class TaskLogR2dbcRepository : TaskLogRepository,
    BaseSyncRepository<TaskLog, TaskLogCreate, TaskLogUpdate, TaskLogTable>(TaskLogTable) {

    override suspend fun ResultRow.toDomain(): TaskLog = TaskLog(
        id = this[TaskLogTable.id].value,
        userId = this[TaskLogTable.userId].value,
        clientId = this[TaskLogTable.clientId],
        version = this[TaskLogTable.version],
        updatedAt = this[TaskLogTable.updatedAt],
        isDeleted = this[TaskLogTable.isDeleted],
        createdAt = this[TaskLogTable.createdAt],
        taskId = this[TaskLogTable.task]?.value,
        clientTaskId = this[TaskLogTable.clientTaskId],
        date = this[TaskLogTable.date],
        completedAt = this[TaskLogTable.completedAt] ?: this[TaskLogTable.createdAt]
    )

    override suspend fun create(data: TaskLogCreate, _userId: Long): TaskLog =
        safeQuery("Не удалось создать запись задачи", logger) {
            val id = TaskLogTable.insertAndGetId {
                it[TaskLogTable.userId] = _userId
                it[TaskLogTable.clientId] = data.clientId
                it[TaskLogTable.task] = data.taskId
                it[TaskLogTable.clientTaskId] = data.clientTaskId
                it[TaskLogTable.date] = data.date
                it[TaskLogTable.completedAt] = data.completedAt
            }.value
            findById(id, _userId) ?: throw AppException.BadRequest("Не удалось создать запись задачи")
        }

    override suspend fun update(data: TaskLogUpdate, userId: Long): TaskLog =
        safeQuery("Не удалось обновить запись задачи", logger) {
            val updatedRows = TaskLogTable.update({
                (TaskLogTable.id eq data.id) and (TaskLogTable.clientId eq data.clientId) and
                        (TaskLogTable.userId eq userId) and (TaskLogTable.version eq data.version)
            }) { statement ->
                statement[TaskLogTable.version] = data.version + 1
                statement[TaskLogTable.updatedAt] = Clock.System.now()
            }
            checkUpdateResult(updatedRows, data.id, userId, "Записи задачи")
            findById(data.id, userId) ?: throw AppException.BadRequest("Не удалось обновить запись задачи")
        }
}
