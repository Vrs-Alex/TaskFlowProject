package com.vrsalex.taskflow.presentation.feature.inbox

import com.vrsalex.taskflow.presentation.model.NoteUiModel
import com.vrsalex.taskflow.presentation.model.TaskUiModel

object InboxContract {

    data class State(
        val isConnected: Boolean = true,
        val searchQuery: String = "",
        val notes: List<NoteUiModel> = emptyList(),
        val overdueTasks: List<TaskUiModel> = emptyList(),
        val filtersBy: SortedListBy = SortedListBy.CreatedAscending
    )


    sealed interface Action {
        data class OnChangeFilter(val f: SortedListBy): Action
        data class OnSearchChange(val s: String): Action
    }


    enum class SortedListBy(val title: Int) {
        CreatedAscending(com.vrsalex.uikit.R.string.filter_created_asc),
        CreatedDescending(com.vrsalex.uikit.R.string.filter_created_desc),
        NameAscending(com.vrsalex.uikit.R.string.filter_name_asc),
        NameDescending(com.vrsalex.uikit.R.string.filter_name_desc),
    }

}
