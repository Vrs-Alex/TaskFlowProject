package com.vrsalex.taskflow.presentation.feature.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetDestination.EventDetail
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetDestination.TaskDetail
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.model.toUiModel
import com.vrsalex.taskflow.presentation.model.toTaskLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.todayIn
import java.time.format.DateTimeFormatter
import kotlin.time.Clock

class TodayViewModel(
    eventRepository: EventRepository,
    private val taskRepository: TaskRepository,
    private val realtimeService: RealtimeService,
    private val itemBottomSheetRouter: ItemBottomSheetRouter
): ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val todayDate = today.toJavaLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM"))

    private val selectedFilterChip = MutableStateFlow(TodayContract.FilterChip.ALL)


    val state = combine(
        realtimeService.isConnected,
        selectedFilterChip,
        eventRepository.getByDate(Clock.System.now()),
        taskRepository.getByDate(Clock.System.now())
    ){ isConnected, selectedFilter, events, tasks ->
        TodayContract.State(
            isConnected = isConnected,
            todayDate = todayDate,
            selectedFilterChip = selectedFilter,
            eventList = events.map { it.toUiModel(isOnlyEnd = true) },
            taskList = tasks.map { it.toUiModel() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        TodayContract.State()
    )


    fun onAction(action: TodayContract.Action) {
        when(action){
            is TodayContract.Action.FilterChipSelected -> {
                selectedFilterChip.update { action.chip }
            }

            is TodayContract.Action.EventClicked -> {
                itemBottomSheetRouter.navigate(EventDetail(action.event.event.base.id))
            }

            is TodayContract.Action.TaskClicked -> {
                itemBottomSheetRouter.navigate(TaskDetail(action.task.task.base.id))
            }

            is TodayContract.Action.TaskCheckBoxToggled -> {
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
