package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.note.NoteRepository
import com.vrsalex.taskflow.domain.item.task.TaskRepository
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.Action
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.SortedListBy
import com.vrsalex.taskflow.presentation.feature.inbox.InboxContract.State
import com.vrsalex.taskflow.presentation.model.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
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

    private val _sort = MutableStateFlow(SortedListBy.CreatedAscending)

    private val _notes = combine(
        noteRepository.get().map { notes -> notes.map { it.toUiModel() } },
        _sort,
        _searchQuery
    ) { notes, sort, searchQuery ->
        val filterList = notes.filter { it.note.name.contains(searchQuery, ignoreCase = true) }
        when (sort) {
            SortedListBy.CreatedAscending -> filterList.sortedBy { it.note.createdAt }
            SortedListBy.CreatedDescending -> filterList.sortedByDescending { it.note.createdAt }
            SortedListBy.NameAscending -> filterList.sortedBy { it.note.name }
            SortedListBy.NameDescending -> filterList.sortedByDescending { it.note.name }
        }

    }.distinctUntilChanged()
        .flowOn(Dispatchers.Default)

    private val _overdueTasks = taskRepository.getOverdue(Clock.System.todayIn(TimeZone.currentSystemDefault()))
        .map { tasks -> tasks.map { it.toUiModel() } }

    val state = combine(
        _isConnected,
        _searchQuery,
        _sort,
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
            is Action.OnChangeFilter -> _sort.update { action.f }
            is Action.OnSearchChange -> _searchQuery.update { action.s }
        }
    }


}
