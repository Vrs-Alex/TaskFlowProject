package com.vrsalex.taskflow.presentation.feature.calendar

import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

object CalendarContract {

    data class State(
        val isServerConnected: Boolean = true,
        val currentDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        val calendarState: CalendarState = CalendarState.COLLAPSED,
        val days: List<CalendarDayState> = emptyList()
    )

    sealed interface Action {
        data class ChangeCalendarState(val state: CalendarState) : Action
        data class UpdateVisibleDate(val date: LocalDate) : Action
    }

    enum class CalendarState {
        COLLAPSED, EXPANDED
    }

    data class CalendarDayState(
        val date: LocalDate,
        val events: List<EventUiModel> = emptyList(),
        val tasks: List<TaskUiModel> = emptyList()
    )

}
