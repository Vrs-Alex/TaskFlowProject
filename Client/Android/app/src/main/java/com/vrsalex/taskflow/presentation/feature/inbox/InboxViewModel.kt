package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.workscape.area.AreaUpdate
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import com.vrsalex.taskflow.domain.workscape.tag.TagUpdate
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class InboxViewModel(
    private val eventRepository: EventRepository,
    private val tagRepository: TagRepository,
    private val areaRepository: AreaRepository,
    private val itemBottomSheetRouter: ItemBottomSheetRouter
) : ViewModel() {

    private val selectedTab = MutableStateFlow(InboxContract.Tab.ITEMS)
    private val searchQuery = MutableStateFlow("")
    private val colorEdit = MutableStateFlow<InboxContract.ColorEditState?>(null)

    private val archivedEvents = searchQuery
        .debounce(300)
        .flatMapLatest { query -> eventRepository.getArchived(query) }

    private val baseState = combine(
        selectedTab,
        searchQuery,
        archivedEvents,
        tagRepository.get(),
        areaRepository.get()
    ) { tab, query, events, tags, areas ->
        val q = query.trim()
        InboxContract.State(
            selectedTab = tab,
            searchQuery = query,
            events = events.map { it.toUiModel() },
            tags = if (q.isBlank()) tags else tags.filter { it.name.contains(q, ignoreCase = true) },
            areas = if (q.isBlank()) areas else areas.filter { it.name.contains(q, ignoreCase = true) }
        )
    }

    val state = combine(baseState, colorEdit) { base, edit ->
        base.copy(colorEdit = edit)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        InboxContract.State()
    )

    fun onAction(action: InboxContract.Action) {
        when (action) {
            is InboxContract.Action.TabSelected -> selectedTab.update { action.tab }

            is InboxContract.Action.SearchQueryChanged -> searchQuery.update { action.query }

            is InboxContract.Action.EventClicked -> {
                itemBottomSheetRouter.navigate(
                    ItemBottomSheetDestination.EventDetail(action.event.event.base.id)
                )
            }

            is InboxContract.Action.TagDeleted -> viewModelScope.launch {
                tagRepository.delete(action.id)
            }

            is InboxContract.Action.AreaDeleted -> viewModelScope.launch {
                areaRepository.delete(action.id)
            }

            is InboxContract.Action.ColorEditStarted -> {
                colorEdit.update { InboxContract.ColorEditState(action.itemId, action.hex, action.isTag) }
            }

            is InboxContract.Action.ColorEditDismissed -> colorEdit.update { null }

            is InboxContract.Action.TagColorSaved -> viewModelScope.launch {
                val tag = state.value.tags.find { it.id == action.id } ?: return@launch
                tagRepository.update(
                    TagUpdate(
                        id = tag.id,
                        serverId = tag.serverId,
                        version = tag.version,
                        color = OptionalField.Defined(action.hex)
                    )
                )
                colorEdit.update { null }
            }

            is InboxContract.Action.AreaColorSaved -> viewModelScope.launch {
                val area = state.value.areas.find { it.id == action.id } ?: return@launch
                areaRepository.update(
                    AreaUpdate(
                        id = area.id,
                        serverId = area.serverId,
                        version = area.version,
                        color = OptionalField.Defined(action.hex)
                    )
                )
                colorEdit.update { null }
            }
        }
    }
}
