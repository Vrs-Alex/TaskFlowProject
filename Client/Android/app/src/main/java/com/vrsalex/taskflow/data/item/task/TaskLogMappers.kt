package com.vrsalex.taskflow.data.item.task

import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity
import com.vrsalex.taskflow.domain.item.task.TaskLog
import com.vrsalex.taskflow.domain.item.task.TaskLogCreate
import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import kotlin.time.Clock

// To Dto

fun TaskLogCreate.toDto(taskServerId: Long?) = TaskLogCreateRequest(
    clientId = id,
    taskId = taskServerId,
    clientTaskId = taskId,
    date = date,
    completedAt = completedAt
)

// To Domain

fun TaskLogEntity.toDomain() = TaskLog(
    id = id,
    serverId = serverId,
    taskId = taskId,
    date = date
)

// To Entity

fun TaskLogCreate.toEntity() = TaskLogEntity(
    id = id,
    serverId = null,
    updatedAt = Clock.System.now(),
    version = 1,
    createdAt = Clock.System.now(),
    isSynced = false,
    isDeleted = false,
    taskId = taskId,
    date = date
)


fun TaskLogDto.toEntity() = TaskLogEntity(
    id = clientId,
    serverId = id,
    updatedAt = updatedAt,
    version = version,
    createdAt = createdAt,
    isSynced = true,
    isDeleted = false,
    taskId = clientTaskId,
    date = date
)
