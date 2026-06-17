package com.vrsalex.taskflow.presentation.feature.add_note.task

import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

object AddItemTaskContract {

    data class State(
        val dueDate: LocalDate? = null,
        val time: LocalTime? = null,
        val recurrenceType: RecurrenceType? = null,
        val recurrenceDays: Set<DayOfWeek> = emptySet(), // только для WEEKLY
        val recurrenceEnd: RecurrenceEnd = RecurrenceEnd.Never,
    )

    /** Условие окончания повтора: бессрочно / до даты / после N повторений. */
    sealed interface RecurrenceEnd {
        data object Never : RecurrenceEnd
        data class OnDate(val date: LocalDate) : RecurrenceEnd
        data class AfterCount(val count: Int) : RecurrenceEnd
    }

    sealed interface Action {
        data class DueDateChanged(val date: LocalDate?) : Action
        data class TimeChanged(val time: LocalTime?) : Action
        data class RecurrenceTypeChanged(val type: RecurrenceType?) : Action
        data class RecurrenceDayToggled(val day: DayOfWeek) : Action
        data class RecurrenceEndChanged(val end: RecurrenceEnd) : Action
    }
}
