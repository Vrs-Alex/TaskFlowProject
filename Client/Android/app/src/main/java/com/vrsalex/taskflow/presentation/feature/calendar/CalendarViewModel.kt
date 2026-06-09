package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.task.TaskLogRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.presentation.model.TaskUiModel
import com.vrsalex.taskflow.presentation.model.toTaskLog
import com.vrsalex.taskflow.presentation.model.toUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class CalendarViewModel(
    private val taskRepository: TaskRepository,
    private val taskLogRepository: TaskLogRepository,
) : ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _selectedDate = MutableStateFlow(today)
    private val _isExpanded = MutableStateFlow(false)
    private val _displayMonth = MutableStateFlow(today.firstDayOfMonth())

    // One-shot scroll command → sent when user taps a date in the calendar
    private val _scrollToDate = Channel<LocalDate>(Channel.CONFLATED)
    val scrollToDate: Flow<LocalDate> = _scrollToDate.receiveAsFlow()

    val state = combine(
        _selectedDate, _isExpanded, _displayMonth,
    ) { selected, expanded, month ->
        Triple(selected, expanded, month)
    }.flatMapLatest { (selected, expanded, month) ->
        val dates = if (expanded) month.monthDates() else selected.weekDates()
        val displayMonth = if (expanded) month else selected.firstDayOfMonth()

        loadTasksForDates(dates).map { tasksByDate ->
            CalendarContract.State(
                today = today,
                selectedDate = selected,
                isExpanded = expanded,
                displayMonth = displayMonth,
                visibleDates = dates,
                tasksByDate = tasksByDate,
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        CalendarContract.State(
            today = today,
            selectedDate = today,
            displayMonth = today.firstDayOfMonth(),
            visibleDates = today.weekDates(),
        )
    )

    fun onAction(action: CalendarContract.Action) {
        when (action) {
            is CalendarContract.Action.DateSelected -> {
                _selectedDate.value = action.date
                if (!_isExpanded.value) _displayMonth.value = action.date.firstDayOfMonth()
                viewModelScope.launch { _scrollToDate.send(action.date) }
            }
            is CalendarContract.Action.DateScrolled -> {
                _selectedDate.value = action.date
            }
            CalendarContract.Action.ToggleExpanded -> {
                val expanding = !_isExpanded.value
                if (expanding) _displayMonth.value = _selectedDate.value.firstDayOfMonth()
                _isExpanded.value = expanding
            }
            CalendarContract.Action.NextMonth ->
                _displayMonth.value = _displayMonth.value.plus(1, DateTimeUnit.MONTH)
            CalendarContract.Action.PreviousMonth ->
                _displayMonth.value = _displayMonth.value.minus(1, DateTimeUnit.MONTH)
            is CalendarContract.Action.TaskCheckBoxToggled -> viewModelScope.launch {
                val task = action.task
                if (task.isCompleted) {
                    task.completedLogId?.let { taskLogRepository.markAsUndone(it) }
                } else {
                    taskLogRepository.markAsDone(task.toTaskLog(forDate = action.date))
                }
            }
        }
    }

    private fun loadTasksForDates(
        dates: List<LocalDate>,
    ): Flow<Map<LocalDate, List<TaskUiModel>>> {
        if (dates.isEmpty()) return flowOf(emptyMap())
        val tz = TimeZone.currentSystemDefault()
        val dateFlows = dates.map { date ->
            taskRepository.getByDate(date.atStartOfDayIn(tz)).map { tasks ->
                date to tasks.map { it.toUiModel() }
            }
        }
        return combine(dateFlows) { pairs ->
            pairs.toMap().filterValues { it.isNotEmpty() }
        }
    }
}

internal fun LocalDate.firstDayOfMonth() = LocalDate(year, month, 1)

internal fun LocalDate.weekDates(): List<LocalDate> {
    val monday = minus(dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
    return (0..6).map { monday.plus(it, DateTimeUnit.DAY) }
}

internal fun LocalDate.monthDates(): List<LocalDate> {
    val first = firstDayOfMonth()
    val daysInMonth = first.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).dayOfMonth
    return (1..daysInMonth).map { LocalDate(year, month, it) }
}
