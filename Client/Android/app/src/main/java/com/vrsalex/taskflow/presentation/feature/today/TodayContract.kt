package com.vrsalex.taskflow.presentation.feature.today

import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.model.EventUiModel
import com.vrsalex.taskflow.presentation.model.TaskUiModel

object TodayContract {

    data class State(
        val isConnected: Boolean = true,
        val todayDate: String = ". . .",
        val selectedFilterChip: FilterChip = FilterChip.ALL,
        val eventList: List<EventUiModel> = emptyList(),
        val taskList: List<TaskUiModel> = emptyList()
    )

    sealed interface Action {
        data class FilterChipSelected(val chip: FilterChip) : Action

        data class EventClicked(val event: EventUiModel) : Action

        data class TaskClicked(val task: TaskUiModel) : Action
        data class TaskCheckBoxToggled(val task: TaskUiModel) : Action
    }


    enum class FilterChip(val title: Int) {
        ALL(R.string.all), EVENT(R.string.events), TASK(R.string.tasks), GOAL(R.string.goals), HABIT(R.string.habits)
    }

}