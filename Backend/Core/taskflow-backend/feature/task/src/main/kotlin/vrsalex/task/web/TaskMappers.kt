package vrsalex.task.web

import vrsalex.core.model.OptionalField
import vrsalex.core.model.toOptional
import vrsalex.item.domain.model.ItemType
import vrsalex.item.web.toDomain
import vrsalex.item.web.toDto
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.task.RecurrenceDto
import vrsalex.shared.api.item.task.RecurrenceTypeDto
import vrsalex.shared.api.item.task.TaskCreateRequest
import vrsalex.shared.api.item.task.TaskDto
import vrsalex.shared.api.item.task.TaskUpdateRequest
import vrsalex.task.domain.Recurrence
import vrsalex.task.domain.RecurrenceType
import vrsalex.task.domain.Task
import vrsalex.task.domain.TaskCreate
import vrsalex.task.domain.TaskUpdate

fun RecurrenceDto.toDomain() = Recurrence(
    type = RecurrenceType.valueOf(type.name),
    interval = interval,
    days = days,
    endDate = endDate,
    count = count
)

fun Recurrence.toDto() = RecurrenceDto(
    type = RecurrenceTypeDto.valueOf(type.name),
    interval = interval,
    days = days,
    endDate = endDate,
    count = count
)

fun OptionalFieldDto<RecurrenceDto?>.toRecurrenceOptional(): OptionalField<Recurrence?> =
    toOptional().map { it?.toDomain() }

fun TaskCreateRequest.toDomain() = TaskCreate(
    base = base.toDomain(ItemType.TASK),
    dueDate = dueDate,
    dueTime = dueTime,
    recurrence = recurrence?.toDomain()
)

fun TaskUpdateRequest.toDomain() = TaskUpdate(
    base = base.toDomain(),
    dueDate = dueDate.toOptional(),
    dueTime = dueTime.toOptional(),
    recurrence = recurrence.toRecurrenceOptional()
)

fun Task.toDto() = TaskDto(
    base = base.toDto(),
    dueDate = dueDate,
    dueTime = dueTime,
    recurrence = recurrence?.toDto()
)
