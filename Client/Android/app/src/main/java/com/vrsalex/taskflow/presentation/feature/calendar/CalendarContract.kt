package com.vrsalex.taskflow.presentation.feature.calendar

import com.vrsalex.taskflow.presentation.model.TaskUiModel
import kotlinx.datetime.LocalDate

object CalendarContract {

    data class State(
        val today: LocalDate = LocalDate(2000, 1, 1),
        val selectedDate: LocalDate = LocalDate(2000, 1, 1),
        val isExpanded: Boolean = false,
        val displayMonth: LocalDate = LocalDate(2000, 1, 1),
        val visibleDates: List<LocalDate> = emptyList(),
        val tasksByDate: Map<LocalDate, List<TaskUiModel>> = emptyMap(),
    )

    sealed interface Action {
        data class DateSelected(val date: LocalDate) : Action   // tap in calendar → also scrolls list
        data class DateScrolled(val date: LocalDate) : Action   // scroll in list → only updates calendar
        data object ToggleExpanded : Action
        data object NextMonth : Action
        data object PreviousMonth : Action
        data class TaskCheckBoxToggled(val task: TaskUiModel, val date: LocalDate) : Action
    }
}
