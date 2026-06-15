package vrsalex.task.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import vrsalex.core.exception.ensure
import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.item.domain.model.*
import vrsalex.shared.api.exception.ErrorCode

enum class RecurrenceType { DAILY, WEEKLY, MONTHLY, YEARLY }

data class Recurrence(
    val type: RecurrenceType,
    val interval: Short = 1,
    val days: Short? = null,
    val endDate: LocalDate? = null,
    val count: Int? = null
)

data class Task(
    val base: Item,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrence: Recurrence?
) : SyncModel by base

data class TaskCreate(
    override val base: ItemCreate,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val recurrence: Recurrence? = null
) : SubItemCreate, SyncClientId by base {
    init {
        ensure(recurrence == null || dueDate != null, ErrorCode.TASK_RECURRENCE_REQUIRES_DATE)
    }
}

data class TaskUpdate(
    override val base: ItemUpdate,
    val dueDate: OptionalField<LocalDate?> = OptionalField.Undefined,
    val dueTime: OptionalField<LocalTime?> = OptionalField.Undefined,
    val recurrence: OptionalField<Recurrence?> = OptionalField.Undefined
) : SubItemUpdate, SyncUpdateModel by base
