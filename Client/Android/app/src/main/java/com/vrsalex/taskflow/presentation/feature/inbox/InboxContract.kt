package com.vrsalex.taskflow.presentation.feature.inbox

import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.task.Task
import kotlin.uuid.Uuid

object InboxContract {

    data class State(
        val tasks: List<Task> = emptyList(),
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = true,
    ) {
        val isEmpty: Boolean get() = tasks.isEmpty() && notes.isEmpty()
    }

    sealed interface Action {
        data class TaskCheckedChange(val task: Task) : Action
        data class ItemClicked(val id: Uuid) : Action
    }



}
