package com.vrsalex.taskflow.presentation.feature.create_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract.Action
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract.SubItemData

class AddItemViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddItemContract.State())
    val state = _state.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.TitleChanged ->
                _state.update { it.copy(title = action.title) }

            is Action.DescriptionChanged ->
                _state.update { it.copy(description = action.description) }

            is Action.TypeChanged ->
                _state.update {
                    it.copy(
                        type = action.type,
                        subItemData = when (action.type) {
                            ItemType.EVENT -> SubItemData.Event()
                            ItemType.TASK -> SubItemData.Task()
                        }
                    )
                }

            is Action.AreaChanged ->
                _state.update { it.copy(selectedArea = action.area) }

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
                    val startDate = data.startDateTime
                        ?: return@launch
                    val endDate = data.endDateTime
                        ?: return@launch
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
                    // taskRepository.create(...)
                    TODO()
                }
                null -> return@launch
            }
        }
    }

}