package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.note.NoteRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.State
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.FilterListBy
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.Action
import com.vrsalex.taskflow.presentation.feature.item.note.toUiModel
import com.vrsalex.taskflow.presentation.feature.task.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class InboxViewModel(
    private val realtimeService: RealtimeService,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _isConnected = realtimeService.isConnected

    private val _searchQuery = MutableStateFlow("")

    private val _filters = MutableStateFlow(FilterListBy.CreatedAscending)

    private val _notes = combine(
        noteRepository.get().map { notes -> notes.map { it.toUiModel() } },
        _filters
    ) { notes, filter ->
        when (filter) {
            FilterListBy.CreatedAscending -> notes.sortedBy { it.note.createdAt }
            FilterListBy.CreatedDescending -> notes.sortedByDescending { it.note.createdAt }
            FilterListBy.NameAscending -> notes.sortedBy { it.note.name }
            FilterListBy.NameDescending -> notes.sortedByDescending { it.note.name }
        }
    }.distinctUntilChanged()

    private val _overdueTasks = taskRepository.getOverdue(Clock.System.todayIn(TimeZone.currentSystemDefault()))
        .map { tasks -> tasks.map { it.toUiModel() } }

    val state = combine(
        _isConnected,
        _searchQuery,
        _filters,
        _notes,
        _overdueTasks
    ){ isSynced, searchQuery, filters, notes, overdueTasks ->
        State(
            isConnected = isSynced,
            searchQuery = searchQuery,
            notes = notes,
            overdueTasks = overdueTasks,
            filtersBy = filters
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        State()
    )


    fun onAction(action: Action){
        when(action){
            is Action.OnChangeFilter -> _filters.update { action.f }
        }
    }


}
