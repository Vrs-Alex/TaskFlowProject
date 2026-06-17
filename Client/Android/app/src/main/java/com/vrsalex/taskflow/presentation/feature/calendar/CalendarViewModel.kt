package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.note.event.EventRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
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
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class CalendarViewModel(
    private val realtimeService: RealtimeService,
    private val eventRepository: EventRepository,
    private val taskRepository: TaskRepository
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
    }.map { byDate ->
        byDate.mapValues { (_, events) -> events.map { it.toUiModel() } }
    }.flowOn(Dispatchers.Default)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksByDate = _visibleDate.flatMapLatest { visibleDate ->
        val from = visibleDate.minus(DatePeriod(months = 1))
        val to = visibleDate.plus(DatePeriod(months = 2))
        taskRepository.observeByDateRange(from, to)
    }.map { byDate ->
        byDate.mapValues { (_, tasks) -> tasks.map { it.toUiModel() } }
    }.flowOn(Dispatchers.Default)



    val state = combine(
        realtimeService.isConnected,
        _currentDate,
        _currentState,
        eventsByDate,
        tasksByDate
    ) { isServerConnected, currentDate, calendarState, eventsByDate, tasksByDate ->
        CalendarContract.State(
            isServerConnected = isServerConnected,
            today = currentDate,
            calendarState = calendarState,
            days = buildDays(eventsByDate, tasksByDate)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        CalendarContract.State(
            today = today,
            days = buildDays(emptyMap(), emptyMap())
        )
    )

    fun onAction(action: CalendarContract.Action) {
        when (action) {
            is CalendarContract.Action.ChangeCalendarState -> _currentState.update { action.state }
            is CalendarContract.Action.UpdateVisibleDate -> _visibleDate.update { action.date }
            is CalendarContract.Action.TaskCheckedChange -> {
                viewModelScope.launch { taskRepository.setDone(action.id, action.date, action.isCompleted) }
            }
            is CalendarContract.Action.ItemClicked -> {}
        }
    }

    private fun buildDays(
        eventsByDate: Map<LocalDate, List<EventUiModel>>,
        tasksByDate: Map<LocalDate, List<TaskUiModel>>
    ): List<CalendarContract.CalendarDayState> =
        dateSpine.map { date ->
            CalendarContract.CalendarDayState(
                date = date,
                events = eventsByDate[date].orEmpty(),
                tasks = tasksByDate[date].orEmpty()
            )
        }

    private companion object {
        const val PAST_DAYS = 90
        const val FUTURE_DAYS = 365
    }
}
