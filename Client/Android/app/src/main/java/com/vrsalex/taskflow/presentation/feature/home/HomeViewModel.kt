package com.vrsalex.taskflow.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.presentation.feature.event.toUiModel
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetDestination.*
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.feature.task.toTaskLog
import com.vrsalex.taskflow.presentation.feature.task.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Clock

class HomeViewModel(
    eventRepository: EventRepository,
    private val taskRepository: TaskRepository,
    private val realtimeService: RealtimeService,
    private val itemBottomSheetRouter: ItemBottomSheetRouter
): ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val todayDate = today.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM"))

    private val selectedFilterChip = MutableStateFlow(HomeContact.FilterChip.ALL)


    val state = combine(
        realtimeService.isConnected,
        selectedFilterChip,
        eventRepository.getByDate(Clock.System.now()),
        taskRepository.getByDate(Clock.System.now())
    ){ isConnected, selectedFilter, events, tasks ->
        HomeContact.State(
            isConnected = isConnected,
            todayDate = todayDate,
            selectedFilterChip = selectedFilter,
            eventList = events.map { it.toUiModel(isOnlyEnd = true) },
            taskList = tasks.map { it.toUiModel() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeContact.State()
    )


    fun onAction(action: HomeContact.Action) {
        when(action){
            is HomeContact.Action.FilterChipSelected -> {
                selectedFilterChip.update { action.chip }
            }

            is HomeContact.Action.EventClicked -> {
                itemBottomSheetRouter.navigate(EventDetail(action.event.event.base.id))
            }

            is HomeContact.Action.TaskClicked -> {
                itemBottomSheetRouter.navigate(TaskDetail(action.task.task.base.id))
            }

            is HomeContact.Action.TaskCheckBoxToggled -> {
                viewModelScope.launch {
                    taskRepository.changeMarkAsDone(
                        data = action.task.toTaskLog(forDate = today),
                        isDone = !action.task.isCompleted
                    )
                }
            }
        }
    }

}
