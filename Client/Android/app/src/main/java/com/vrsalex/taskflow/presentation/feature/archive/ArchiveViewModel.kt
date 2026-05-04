package com.vrsalex.taskflow.presentation.feature.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetDestination
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetRouter
import com.vrsalex.taskflow.presentation.feature.event.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class ArchiveViewModel(
    private val eventRepository: EventRepository,
    private val itemBottomSheetRouter: ItemBottomSheetRouter
) : ViewModel() {

    private val selectedFilter = MutableStateFlow(ArchiveContract.Filter.ALL)
    private val searchQuery = MutableStateFlow("")

    private val archivedEvents = searchQuery
        .debounce(300)
        .flatMapLatest { query -> eventRepository.getArchived(query) }

    val state = combine(
        selectedFilter,
        searchQuery,
        archivedEvents
    ) { filter, query, events ->
        val filteredEvents = when (filter) {
            ArchiveContract.Filter.ALL, ArchiveContract.Filter.EVENT -> events.map { it.toUiModel() }
            ArchiveContract.Filter.TASK -> emptyList()
        }
        ArchiveContract.State(
            selectedFilter = filter,
            searchQuery = query,
            events = filteredEvents
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ArchiveContract.State()
    )

    fun onAction(action: ArchiveContract.Action) {
        when (action) {
            is ArchiveContract.Action.FilterSelected -> selectedFilter.update { action.filter }
            is ArchiveContract.Action.SearchQueryChanged -> searchQuery.update { action.query }
            is ArchiveContract.Action.EventClicked -> {
                itemBottomSheetRouter.navigate(
                    ItemBottomSheetDestination.EventDetail(action.event.event.base.id)
                )
            }
        }
    }
}
