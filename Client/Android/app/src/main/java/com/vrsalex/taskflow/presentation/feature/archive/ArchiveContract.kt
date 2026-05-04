package com.vrsalex.taskflow.presentation.feature.archive

import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.feature.event.EventUiModel

object ArchiveContract {

    data class State(
        val selectedFilter: Filter = Filter.ALL,
        val searchQuery: String = "",
        val events: List<EventUiModel> = emptyList()
    )

    sealed interface Action {
        data class FilterSelected(val filter: Filter) : Action
        data class SearchQueryChanged(val query: String) : Action
        data class EventClicked(val event: EventUiModel) : Action
    }

    enum class Filter(val title: Int) {
        ALL(R.string.all),
        EVENT(R.string.events),
        TASK(R.string.tasks)
    }
}
