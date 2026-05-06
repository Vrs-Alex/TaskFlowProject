package vrsalex.task.web

import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import vrsalex.shared.api.item.task.TaskLogUpdateRequest
import vrsalex.task.domain.TaskLog
import vrsalex.task.domain.TaskLogCreate
import vrsalex.task.domain.TaskLogUpdate

fun TaskLogCreateRequest.toDomain() = TaskLogCreate(
    clientId = clientId,
    taskId = taskId,
    clientTaskId = clientTaskId,
    date = date,
    completedAt = completedAt
)

fun TaskLogUpdateRequest.toDomain() = TaskLogUpdate(
    id = id,
    clientId = clientId,
    version = version
)

fun TaskLog.toDto() = TaskLogDto(
    id = id,
    clientId = clientId,
    version = version,
    updatedAt = updatedAt,
    createdAt = createdAt,
    taskId = taskId,
    clientTaskId = clientTaskId,
    date = date,
    completedAt = completedAt
)
