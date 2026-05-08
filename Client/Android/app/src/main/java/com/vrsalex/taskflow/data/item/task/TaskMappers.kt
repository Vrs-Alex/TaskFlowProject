package com.vrsalex.taskflow.data.item.task

import com.vrsalex.taskflow.data.item.base.toCreateDto
import com.vrsalex.taskflow.data.item.base.toDomain
import com.vrsalex.taskflow.data.item.base.toDto
import com.vrsalex.taskflow.data.item.base.toUpdateDto
import com.vrsalex.taskflow.data.local.db.entity.item.TaskEntity
import com.vrsalex.taskflow.data.local.db.relation.TaskRelation
import com.vrsalex.taskflow.data.workspace.area.toDomain
import com.vrsalex.taskflow.data.workspace.tag.toDomain
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.task.Recurrence
import com.vrsalex.taskflow.domain.item.task.RecurrenceType
import com.vrsalex.taskflow.domain.item.task.Task
import com.vrsalex.taskflow.domain.item.task.TaskCreate
import com.vrsalex.taskflow.domain.item.task.TaskLog
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import kotlinx.datetime.LocalDate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.task.RecurrenceDto
import vrsalex.shared.api.item.task.RecurrenceTypeDto
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskUpdateRequest

// TO DTO

fun TaskCreate.toDto() = TaskCreateRequest(
    base = base.toDto(),
    dueDate = dueDate,
    dueTime = dueTime,
    recurrence = recurrence?.toDto()
)

fun Task.toCreateDto() = TaskCreateRequest(
    base = base.toCreateDto(),
    dueDate = dueDate,
    dueTime = dueTime,
    recurrence = recurrence?.toDto()
)

fun Task.toUpdateDto() = TaskUpdateRequest(
    base = base.toUpdateDto(),
    dueDate = OptionalFieldDto.Defined(dueDate),
    dueTime = OptionalFieldDto.Defined(dueTime),
    recurrence = OptionalFieldDto.Defined(recurrence?.toDto())
)

fun TaskUpdate.toDto(): TaskUpdateRequest? = TaskUpdateRequest(
    base = base.toDto() ?: return null,
    dueDate = dueDate.toOptionalDto(),
    dueTime = dueTime.toOptionalDto(),
    recurrence = recurrence.map { it?.toDto() }.toOptionalDto()
)

fun Recurrence.toDto() = RecurrenceDto(
    type = RecurrenceTypeDto.valueOf(type.name),
    interval = interval.toShort(),
    days = days,
    endDate = endDate,
    count = count
)

// TO DOMAIN

fun TaskRelation.toDomain(forDate: LocalDate = task.dueDate) = Task(
    base = item.toDomain().copy(
        tags = tags.map { it.toDomain() },
        area = area?.toDomain()
    ),
    dueDate = task.dueDate,
    dueTime = task.dueTime,
    recurrence = task.toRecurrence(),
    completedLogs = taskLogs.filter { it.date == forDate && !it.isDeleted }.map { it.toDomain() },
    isSynced = item.isSynced && taskLogs.all { it.isSynced }
)

fun TaskEntity.toRecurrence(): Recurrence? {
    val type = recurrenceType?.let { RecurrenceType.valueOf(it) } ?: return null
    return Recurrence(
        type = type,
        interval = recurrenceInterval,
        days = recurrenceDays,
        endDate = recurrenceEndDate,
        count = recurrenceCount
    )
}

// TO ENTITY

fun TaskDto.toEntity() = TaskEntity(
    itemId = base.clientId,
    dueDate = dueDate,
    dueTime = dueTime,
    recurrenceType = recurrence?.type?.name,
    recurrenceInterval = recurrence?.interval?.toInt() ?: 1,
    recurrenceDays = recurrence?.days,
    recurrenceEndDate = recurrence?.endDate,
    recurrenceCount = recurrence?.count
)

fun TaskCreate.toEntity() = TaskEntity(
    itemId = base.id,
    dueDate = dueDate,
    dueTime = dueTime,
    recurrenceType = recurrence?.type?.name,
    recurrenceInterval = recurrence?.interval ?: 1,
    recurrenceDays = recurrence?.days,
    recurrenceEndDate = recurrence?.endDate,
    recurrenceCount = recurrence?.count
)
