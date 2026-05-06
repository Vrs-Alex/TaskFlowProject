package vrsalex.shared.api.item.task

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.common.SyncDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest

@Serializable
enum class RecurrenceTypeDto { DAILY, WEEKLY, MONTHLY, YEARLY }

@Serializable
data class RecurrenceDto(
    val type: RecurrenceTypeDto,
    val interval: Short = 1,
    val days: Short? = null,
    val endDate: LocalDate? = null,
    val count: Int? = null
)

@Serializable
data class TaskDto(
    val base: ItemDto,
    val dueDate: LocalDate,
    val dueTime: LocalTime?,
    val recurrence: RecurrenceDto?
) : SyncDto by base

@Serializable
data class TaskCreateRequest(
    val base: ItemCreateRequest,
    val dueDate: LocalDate,
    val dueTime: LocalTime? = null,
    val recurrence: RecurrenceDto? = null
)

@Serializable
data class TaskUpdateRequest(
    val base: ItemUpdateRequest,
    val dueDate: OptionalFieldDto<LocalDate> = OptionalFieldDto.Undefined,
    val dueTime: OptionalFieldDto<LocalTime?> = OptionalFieldDto.Undefined,
    val recurrence: OptionalFieldDto<RecurrenceDto?> = OptionalFieldDto.Undefined
)
