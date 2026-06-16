package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.presentation.feature.calendar.model.CalendarDayState
import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.taskflow.presentation.model.note.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class CalendarViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val dateSpine: List<LocalDate> = buildList {
        val startDate = today.minus(DatePeriod(days = PAST_DAYS))
        repeat(PAST_DAYS + 1 + FUTURE_DAYS) { day ->
            add(startDate.plus(DatePeriod(days = day)))
        }
    }

    private val _currentDate = MutableStateFlow(today)
    private val _visibleDate = MutableStateFlow(today)
    private val _currentState = MutableStateFlow(CalendarContract.CalendarState.COLLAPSED)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val eventsByDate = _visibleDate.flatMapLatest { visibleDate ->
        val from = visibleDate.minus(DatePeriod(months = 1))
        val to = visibleDate.plus(DatePeriod(months = 2))
        eventRepository.observeByDateRange(from, to)
    }.map { events ->
        val tz = TimeZone.currentSystemDefault()
        val map = mutableMapOf<LocalDate, MutableList<EventUiModel>>()
        for (event in events) {
            val startLocalDate = event.startDate.toLocalDateTime(tz).date
            val endLocalDate = event.endDate?.toLocalDateTime(tz)?.date ?: startLocalDate
            var trackingDate = startLocalDate
            while (trackingDate <= endLocalDate) {
                map.getOrPut(trackingDate) { mutableListOf() }.add(event.toUiModel())
                trackingDate = trackingDate.plus(DatePeriod(days = 1))
            }
        }
        map
    }.flowOn(Dispatchers.Default)

    val state = combine(
        _currentDate,
        _currentState,
        eventsByDate
    ) { currentDate, calendarState, byDate ->
        CalendarContract.State(
            currentDate = currentDate,
            calendarState = calendarState,
            days = buildDays(byDate)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        CalendarContract.State(
            currentDate = today,
            days = buildDays(emptyMap())
        )
    )

    fun onAction(action: CalendarContract.Action) {
        when (action) {
            is CalendarContract.Action.ChangeCalendarState -> _currentState.update { action.state }
            is CalendarContract.Action.UpdateVisibleDate -> _visibleDate.update { action.date }
        }
    }

    private fun buildDays(byDate: Map<LocalDate, List<EventUiModel>>): List<CalendarDayState> =
        dateSpine.map { date ->
            CalendarDayState(date = date, events = byDate[date].orEmpty())
        }

    private companion object {
        const val PAST_DAYS = 90
        const val FUTURE_DAYS = 365
    }
}
