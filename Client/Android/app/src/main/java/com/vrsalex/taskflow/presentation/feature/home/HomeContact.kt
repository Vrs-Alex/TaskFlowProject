package com.vrsalex.taskflow.presentation.feature.home

import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.feature.event.EventUiModel

object HomeContact {

    data class State(
        val isConnected: Boolean = false,
        val todayDate: String = ". . .",
        val selectedFilterChip: FilterChip = FilterChip.ALL,
        val eventList: List<EventUiModel> = emptyList()
    )

    sealed interface Action {
        data class FilterChipSelected(val chip: FilterChip) : Action
        data class EventClicked(val event: EventUiModel) : Action
    }


    enum class FilterChip(val title: Int) {
        ALL(R.string.all), EVENT(R.string.events), TASK(R.string.tasks), GOAL(R.string.goals), HABIT(R.string.habits)
    }

}