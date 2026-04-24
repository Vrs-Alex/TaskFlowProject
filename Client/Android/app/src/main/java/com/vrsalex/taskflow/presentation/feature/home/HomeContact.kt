package com.vrsalex.taskflow.presentation.feature.home

import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.event.Event

object HomeContact {

    data class State(
        val todayDate: String = "",
        val selectedFilterChip: FilterChip = FilterChip.ALL,
        val eventList: List<Event> = emptyList()
    )

    sealed interface Action {
        data class FilterChipSelected(val chip: FilterChip) : Action
    }


    enum class FilterChip(val title: Int) {
        ALL(R.string.all), EVENT(R.string.event), TASK(R.string.task), GOAL(R.string.goal), HABIT(R.string.habit)
    }

}