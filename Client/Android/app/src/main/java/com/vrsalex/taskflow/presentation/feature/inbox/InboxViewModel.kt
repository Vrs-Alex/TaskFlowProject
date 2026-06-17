package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.presentation.model.note.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class InboxViewModel(
    private val realtimeService: RealtimeService,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _isConnected = realtimeService.isConnected
    private val _sortedBy = MutableStateFlow(InboxContract.SortedListBy.PriorityAscending)

    private val sortedNotes = combine(noteRepository.observeInbox(), _sortedBy) { notes, sorted ->
        when (sorted) {
            InboxContract.SortedListBy.PriorityAscending -> notes.sortedBy { it.priority.sortRank }
            InboxContract.SortedListBy.PriorityDescending -> notes.sortedByDescending { it.priority.sortRank }
            InboxContract.SortedListBy.CreatedAscending -> notes.sortedBy { it.syncModel.createdAt }
            InboxContract.SortedListBy.CreatedDescending -> notes.sortedByDescending { it.syncModel.createdAt }
            InboxContract.SortedListBy.NameAscending -> notes.sortedBy { it.name.value }
            InboxContract.SortedListBy.NameDescending -> notes.sortedByDescending { it.name.value }
        }
    }.map { it.map { n -> n.toUiModel() } }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)

    private val sortedTasks = combine(taskRepository.observeInbox(), _sortedBy) { tasks, sorted ->
        when (sorted) {
            InboxContract.SortedListBy.PriorityAscending -> tasks.sortedBy { it.note.priority.sortRank }
            InboxContract.SortedListBy.PriorityDescending -> tasks.sortedByDescending { it.note.priority.sortRank }
            InboxContract.SortedListBy.CreatedAscending -> tasks.sortedBy { it.syncModel.createdAt }
            InboxContract.SortedListBy.CreatedDescending -> tasks.sortedByDescending { it.syncModel.createdAt }
            InboxContract.SortedListBy.NameAscending -> tasks.sortedBy { it.note.name.value }
            InboxContract.SortedListBy.NameDescending -> tasks.sortedByDescending { it.note.name.value }
        }
    }.map { it.map { t -> t.toUiModel() } }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)

    val state = combine(
        _isConnected,
        _sortedBy,
        sortedNotes,
        sortedTasks,
    ) { serverConnected, sorted, notes, tasks ->
        InboxContract.State(
            isServerConnect = serverConnected,
            currentSortedListBy = sorted,
            tasks = tasks,
            notes = notes,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), InboxContract.State())

    fun onAction(action: InboxContract.Action) {
        when (action) {
            is InboxContract.Action.OnChangeSorted -> _sortedBy.update { action.sort }
            is InboxContract.Action.TaskCheckedChange -> viewModelScope.launch {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                taskRepository.setDone(action.id, today, !action.isCompleted)
            }
            is InboxContract.Action.ItemClicked -> Unit // навигация к деталям — позже
        }
    }
}
