package com.vrsalex.taskflow.presentation.feature.inbox

import com.vrsalex.taskflow.presentation.model.note.NoteUiModel
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
import kotlin.uuid.Uuid

object InboxContract {

    data class State(
        val isServerConnect: Boolean = true,
        val currentSortedListBy: SortedListBy = SortedListBy.CreatedAscending,
        val tasks: List<TaskUiModel> = emptyList(),
        val notes: List<NoteUiModel> = emptyList(),
        val isLoading: Boolean = true,
    ) {
        val isEmpty: Boolean get() = tasks.isEmpty() && notes.isEmpty()
    }

    sealed interface Action {
        data class OnChangeSorted(val sort: SortedListBy) : Action
        data class TaskCheckedChange(val id: Uuid, val isCompleted: Boolean) : Action
        data class ItemClicked(val id: Uuid) : Action
    }

    enum class SortedListBy(val title: Int) {
        PriorityAscending(com.vrsalex.uikit.R.string.sort_priority_asc),
        PriorityDescending(com.vrsalex.uikit.R.string.sort_priority_desc),
        CreatedAscending(com.vrsalex.uikit.R.string.sort_created_asc),
        CreatedDescending(com.vrsalex.uikit.R.string.sort_created_desc),
        NameAscending(com.vrsalex.uikit.R.string.sort_name_asc),
        NameDescending(com.vrsalex.uikit.R.string.sort_name_desc),
    }
}
