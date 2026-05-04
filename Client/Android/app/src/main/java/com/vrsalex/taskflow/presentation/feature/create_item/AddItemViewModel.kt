package com.vrsalex.taskflow.presentation.feature.create_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.data.workspace.tag.toUpdateDto
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import com.vrsalex.taskflow.domain.workscape.area.AreaCreate
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.workscape.tag.TagCreate
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract.Action
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract.SubItemData
import com.vrsalex.taskflow.presentation.feature.workspace.area.toUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class AddItemViewModel(
    private val areaRepository: AreaRepository,
    private val tagRepository: TagRepository,
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _isVisible = MutableStateFlow(false)
    val isVisibleState = _isVisible.asStateFlow()

    fun onVisibilityChanged(isVisible: Boolean) {
        _isVisible.value = isVisible
    }

    private val _state = MutableStateFlow(AddItemContract.State())
    val state = combine(
        areaRepository.get(),
        tagRepository.get(),
        _state
    ) { areas, tags, state ->
        AddItemContract.State(
            activeSelector = state.activeSelector,
            selectorSearch = state.selectorSearch,
            title = state.title,
            description = state.description,
            type = state.type,
            availableAreas = areas.map { it.toUiModel() },
            selectedArea = state.selectedArea,
            availableTags = tags.map { it.toUiModel() },
            selectedTags = state.selectedTags,
            subItemData = state.subItemData
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddItemContract.State()
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.ShowSelector -> _state.update {
                if (it.activeSelector == action.type) {
                    it.copy(activeSelector = SelectorType.NONE)
                } else
                    it.copy(activeSelector = action.type, selectorSearch = "")
            }
            is Action.HideSelector -> _state.update {
                it.copy(activeSelector = SelectorType.NONE, selectorSearch = "")
            }
            is Action.SelectorSearchChanged -> _state.update {
                it.copy(selectorSearch = action.query)
            }
            is Action.SelectorCreate -> {
                viewModelScope.launch {
                    when (state.value.activeSelector) {
                        SelectorType.TAGS -> {
                            _state.update { it.copy(selectorSearch = "") }
                            tagRepository.create(TagCreate(name = action.name))
                        }
                        SelectorType.AREA -> {
                            _state.update { it.copy(selectorSearch = "") }
                            areaRepository.create(AreaCreate(name = action.name))
                        }
                        SelectorType.NONE -> {}
                    }
                }
            }
            is Action.TitleChanged -> _state.update {
                it.copy(title = action.title)
            }
            is Action.DescriptionChanged -> _state.update {
                it.copy(description = action.description)
            }
            is Action.TypeChanged -> _state.update {
                it.copy(
                    type = action.type,
                    subItemData = when (action.type) {
                        ItemType.EVENT -> SubItemData.Event()
                        ItemType.TASK -> SubItemData.Task()
                    }
                )
            }
            is Action.AreaChanged -> _state.update {
                it.copy(
                    selectedArea = action.area,
                    activeSelector = SelectorType.NONE,
                    selectorSearch = ""
                )
            }
            is Action.TagToggled -> _state.update { state ->
                val isSelected = state.selectedTags.any { it.id == action.tag.id }
                state.copy(
                    selectedTags = if (isSelected)
                        state.selectedTags.filter { it.id != action.tag.id }
                    else
                        state.selectedTags + action.tag
                )
            }
            is Action.EventAction -> handleEventAction(action)
            is Action.TaskAction -> handleTaskAction(action)
            is Action.Save -> onSave()
        }
    }

    private fun handleEventAction(action: Action.EventAction) {
        _state.update { state ->
            val event = state.subItemData as? SubItemData.Event ?: return
            state.copy(
                subItemData = when (action) {
                    is Action.EventAction.StartDateTimeChanged ->
                        event.copy(startDateTime = action.dateTime)
                    is Action.EventAction.EndDateTimeChanged ->
                        event.copy(endDateTime = action.dateTime)
                    is Action.EventAction.IsAllDayChanged ->
                        event.copy(isAllDay = action.isAllDay)
                    is Action.EventAction.LocationChanged ->
                        event.copy(location = action.location)
                }
            )
        }
    }

    private fun handleTaskAction(action: Action.TaskAction) {
        _state.update { state ->
            val task = state.subItemData as? SubItemData.Task ?: return
            state.copy(
                subItemData = when (action) {
                    is Action.TaskAction.DueDateChanged ->
                        task.copy(dueDate = action.dateTime)
                    is Action.TaskAction.PriorityChanged ->
                        task.copy(priority = action.priority)
                }
            )
        }
    }

    private fun onSave() {
        val state = _state.value
        if (state.title.isBlank()) return

        viewModelScope.launch {
            val base = ItemCreate(
                name = state.title,
                description = state.description.ifBlank { null },
                type = state.type,
                priority = 0,
                areaId = state.selectedArea?.id,
                tagIds = state.selectedTags.map { it.id }
            )

            when (val data = state.subItemData) {
                is SubItemData.Event -> {
                    val startDate = data.startDateTime ?: return@launch
                    val endDate = data.endDateTime ?: return@launch
                    if (startDate > endDate) return@launch

                    eventRepository.create(
                        EventCreate(
                            base = base,
                            startDate = startDate.toInstant(TimeZone.currentSystemDefault()),
                            endDate = endDate.toInstant(TimeZone.currentSystemDefault()),
                            isAllDay = data.isAllDay,
                            location = data.location?.ifBlank { null }
                        )
                    )
                }
                is SubItemData.Task -> {
                    TODO()
                }
                null -> return@launch
            }

            _state.value = AddItemContract.State()
            _isVisible.value = false
        }
    }
}