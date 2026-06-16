package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.presentation.common.extension.weekdayShortRes
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
): ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val availableDates = buildList {
        val startDate = today.minus(DatePeriod(days = 90))
        repeat(90 + 1 + 365) { day -> // -90 .. today 1 .. +365
            val date = startDate.plus(DatePeriod(days = day))
            add(CalendarContract.Date(date = date, date.weekdayShortRes()))
        }
    }

    private val _currentDate = MutableStateFlow(today)
    private val _visibleDate = MutableStateFlow(today)
    private val _currentState = MutableStateFlow(CalendarContract.CalendarState.COLLAPSED)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events = _visibleDate.flatMapLatest { visibleDate ->
        val from = visibleDate.minus(DatePeriod(months = 1))
        val to = visibleDate.plus(DatePeriod(months = 2))
        eventRepository.observeByDateRange(from, to)
    }

    private val mappedEventsByDateFlow = _events.map { eventsList ->
            val tz = TimeZone.currentSystemDefault()
            val eventsMap = mutableMapOf<LocalDate, MutableList<EventUiModel>>()

            for (event in eventsList) {
                val startLocalDate = event.startDate.toLocalDateTime(tz).date
                val endLocalDate = event.endDate?.toLocalDateTime(tz)?.date ?: startLocalDate

                var trackingDate = startLocalDate
                while (trackingDate <= endLocalDate) {
                    eventsMap.getOrPut(trackingDate) { mutableListOf() }.add(event.toUiModel())
                    trackingDate = trackingDate.plus(DatePeriod(days = 1))
                }
            }
            eventsMap
        }
        .flowOn(Dispatchers.Default)

    val state = combine(
        _currentDate,
        _currentState,
        mappedEventsByDateFlow
    ){ currentDate, state, eventsByDate ->
        val mappedDates = availableDates.map { dayState ->
            CalendarDayState(
                date = dayState.date,
                events = eventsByDate[dayState.date]?: emptyList()
            )
        }

        CalendarContract.State(
            currentDate = currentDate,
            calendarState = state,
            availableDates = availableDates,
            eventList = mappedDates
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CalendarContract.State(availableDates = availableDates))


    fun onAction(action: CalendarContract.Action){
        when(action) {
            is CalendarContract.Action.ChangeCalendarState -> _currentState.update { action.state }
            is CalendarContract.Action.ChangeCurrentDate -> _currentDate.update { action.date }
            is CalendarContract.Action.UpdateVisibleDate -> _visibleDate.update { action.date }
        }
    }

}