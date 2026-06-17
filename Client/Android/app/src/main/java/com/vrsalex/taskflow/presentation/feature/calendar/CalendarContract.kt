package com.vrsalex.taskflow.presentation.feature.calendar

import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.uuid.Uuid

object CalendarContract {

    data class State(
        val isServerConnected: Boolean = true,
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        val calendarState: CalendarState = CalendarState.COLLAPSED,
        val days: List<CalendarDayState> = emptyList()
    )

    sealed interface Action {
        data class ChangeCalendarState(val state: CalendarState) : Action
        data class UpdateVisibleDate(val date: LocalDate) : Action
        data class TaskCheckedChange(val id: Uuid, val date: LocalDate, val isCompleted: Boolean) : Action
        data class ItemClicked(val id: Uuid) : Action
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
