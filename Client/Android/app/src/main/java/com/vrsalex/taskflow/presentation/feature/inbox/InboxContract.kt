package com.vrsalex.taskflow.presentation.feature.inbox

import com.vrsalex.taskflow.presentation.feature.item.note.NoteUiModel
import com.vrsalex.taskflow.presentation.feature.task.TaskUiModel

object InboxContract {

    data class State(
        val isConnected: Boolean = true,
        val searchQuery: String = "",
        val notes: List<NoteUiModel> = emptyList(),
        val overdueTasks: List<TaskUiModel> = emptyList(),
        val filtersBy: FilterListBy = FilterListBy.CreatedAscending
    )


    sealed interface Action {
        data class OnChangeFilter(val f: FilterListBy): Action
    }


    enum class FilterListBy(val title: Int) {
        CreatedAscending(com.vrsalex.uikit.R.string.filter_created_asc),
        CreatedDescending(com.vrsalex.uikit.R.string.filter_created_desc),
        NameAscending(com.vrsalex.uikit.R.string.filter_name_asc),
        NameDescending(com.vrsalex.uikit.R.string.filter_name_desc),
    }

}
