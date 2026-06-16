package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class InboxViewModel(
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    val state = combine(
        noteRepository.observeInbox(),
        taskRepository.observeInbox(),
    ) { notes, tasks ->
        InboxContract.State(
            tasks = tasks,
            notes = notes,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        InboxContract.State()
    )

    fun onAction(action: InboxContract.Action) {
        when (action) {
            is InboxContract.Action.TaskCheckedChange -> viewModelScope.launch {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                taskRepository.setDone(action.task.note.syncModel.id, today, !action.task.isCompleted)
            }
            is InboxContract.Action.ItemClicked -> Unit
        }
    }
}
