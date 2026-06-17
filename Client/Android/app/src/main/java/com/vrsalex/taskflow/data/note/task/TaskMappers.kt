package com.vrsalex.taskflow.data.note.task

import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncColumns
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import com.vrsalex.taskflow.data.note.toItemCreateRequest
import com.vrsalex.taskflow.data.note.toItemUpdateRequest
import com.vrsalex.taskflow.data.note.toNote
import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import com.vrsalex.taskflow.domain.note.task.Task
import com.vrsalex.taskflow.domain.note.task.TaskCreate
import com.vrsalex.taskflow.domain.note.task.TaskLog
import com.vrsalex.taskflow.domain.note.task.TaskLogCreate
import kotlinx.datetime.LocalDate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.task.RecurrenceDto
import vrsalex.shared.api.item.task.RecurrenceTypeDto
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskLogCreateRequest
import vrsalex.shared.api.item.task.TaskLogDto
import vrsalex.shared.api.item.task.TaskUpdateRequest

fun TaskRelation.toDomain(forDate: LocalDate? = task.dueDate): Task = Task(
    dueDate = task.dueDate,
    dueTime = task.dueTime,
    recurrenceType = task.recurrenceType,
    recurrenceDays = task.recurrenceDays,
    recurrenceEndDate = task.recurrenceEndDate,
    recurrenceCount = task.recurrenceCount,
    recurrenceInterval = task.recurrenceInterval,
    isCompleted = logs.any { !it.sync.isDeleted && (it.date == forDate || forDate == null) && it.completedAt != null },
    note = note.toNote(area, tags),
)

fun TaskCreate.toTaskEntity(): TaskEntity = TaskEntity(
    id = note.syncModelCreate.id,
    dueDate = dueDate,
    dueTime = dueTime,
    recurrenceType = recurrenceType,
    recurrenceDays = recurrenceDays,
    recurrenceEndDate = recurrenceEndDate,
    recurrenceCount = recurrenceCount,
    recurrenceInterval = recurrenceInterval,
)

fun TaskLogEntity.toDomain(): TaskLog = TaskLog(
    taskId = taskId,
    date = date,
    completedAt = completedAt,
    syncModel = sync.toSyncModel(id),
)

fun TaskLogCreate.toEntity(): TaskLogEntity = TaskLogEntity(
    id = syncModelCreate.id,
    taskId = taskId,
    date = date,
    completedAt = completedAt,
    sync = newLocalSync(),
)

// --- Применение с сервера (PULL) ---

fun TaskDto.toTaskEntity(): TaskEntity = TaskEntity(
    id = base.clientId,
    dueDate = dueDate,
    dueTime = dueTime,
    recurrenceType = recurrence?.type?.toDomain(),
    recurrenceDays = recurrence?.days,
    recurrenceEndDate = recurrence?.endDate,
    recurrenceCount = recurrence?.count,
    recurrenceInterval = recurrence?.interval ?: 1,
)

fun RecurrenceTypeDto.toDomain(): RecurrenceType = when (this) {
    RecurrenceTypeDto.DAILY -> RecurrenceType.DAILY
    RecurrenceTypeDto.WEEKLY -> RecurrenceType.WEEKLY
    RecurrenceTypeDto.MONTHLY -> RecurrenceType.MONTHLY
    RecurrenceTypeDto.YEARLY -> RecurrenceType.YEARLY
}

fun TaskLogDto.toEntity(): TaskLogEntity = TaskLogEntity(
    id = clientId,
    taskId = clientTaskId,
    date = date,
    completedAt = completedAt,
    sync = toSyncColumns(),
)

// --- Локальная dirty-запись → запросы на сервер (push) ---

fun RecurrenceType.toDto(): RecurrenceTypeDto = when (this) {
    RecurrenceType.DAILY -> RecurrenceTypeDto.DAILY
    RecurrenceType.WEEKLY -> RecurrenceTypeDto.WEEKLY
    RecurrenceType.MONTHLY -> RecurrenceTypeDto.MONTHLY
    RecurrenceType.YEARLY -> RecurrenceTypeDto.YEARLY
}

fun TaskEntity.toRecurrenceDto(): RecurrenceDto? {
    val type = recurrenceType?.toDto() ?: return null
    return RecurrenceDto(
        type = type,
        interval = recurrenceInterval,
        days = recurrenceDays,
        endDate = recurrenceEndDate,
        count = recurrenceCount,
    )
}

fun TaskRelation.toCreateRequest(): TaskCreateRequest = TaskCreateRequest(
    base = note.toItemCreateRequest(tags.map { it.id }),
    dueDate = task.dueDate,
    dueTime = task.dueTime,
    recurrence = task.toRecurrenceDto(),
)

fun TaskRelation.toUpdateRequest(): TaskUpdateRequest = TaskUpdateRequest(
    base = note.toItemUpdateRequest(tags.map { it.id }),
    dueDate = OptionalFieldDto.Defined(task.dueDate),
    dueTime = OptionalFieldDto.Defined(task.dueTime),
    recurrence = OptionalFieldDto.Defined(task.toRecurrenceDto()),
)

fun TaskLogEntity.toCreateRequest(): TaskLogCreateRequest = TaskLogCreateRequest(
    clientId = id,
    taskId = null, // сервер сопоставит задачу по clientTaskId
    clientTaskId = taskId,
    date = date,
    completedAt = requireNotNull(completedAt) { "completedAt обязателен для отметки выполнения" },
)
