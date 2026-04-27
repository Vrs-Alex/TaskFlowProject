package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.add

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

class AddBottomSheetViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddBottomSheetContract.State())
    val state = _state.asStateFlow()

    fun onAction(action: AddBottomSheetContract.Action) {
        when (action) {
            is AddBottomSheetContract.Action.TitleChanged ->
                _state.update { it.copy(title = action.title) }

            is AddBottomSheetContract.Action.DescriptionChanged ->
                _state.update { it.copy(description = action.description) }

            is AddBottomSheetContract.Action.TypeChanged ->
                _state.update { it.copy(type = action.type) }

            is AddBottomSheetContract.Action.AreaChanged ->
                _state.update { it.copy(area = action.area) }

            is AddBottomSheetContract.Action.TagToggled -> _state.update { state ->
                val isSelected = state.selectedTags.any { it.id == action.tag.id }
                state.copy(
                    selectedTags = if (isSelected)
                        state.selectedTags.filter { it.id != action.tag.id }
                    else
                        state.selectedTags + action.tag
                )
            }

            is AddBottomSheetContract.Action.StartDateTimeChanged ->
                _state.update { it.copy(startDateTime = action.dateTime) }

            is AddBottomSheetContract.Action.EndDateTimeChanged ->
                _state.update { it.copy(endDateTime = action.dateTime) }

            is AddBottomSheetContract.Action.IsAllDayChanged ->
                _state.update { it.copy(isAllDay = action.isAllDay) }

            is AddBottomSheetContract.Action.LocationChanged ->
                _state.update { it.copy(location = action.location) }

            is AddBottomSheetContract.Action.Save -> onSave()
        }
    }

    private fun onSave() {
        val state = _state.value
        if (state.title.isBlank()) return
        viewModelScope.launch {
            when(state.type){
                ItemType.EVENT -> {
                    eventRepository.create(
                        EventCreate(
                            base = ItemCreate(
                                name = state.title,
                                description = state.description.ifBlank { null },
                                type = ItemType.EVENT,
                                priority = 0,
                                areaId = state.area?.id,
                                tagIds = state.selectedTags.map { it.id }
                            ),
                            startDate = state.startDateTime!!.toInstant(TimeZone.currentSystemDefault()),
                            endDate = state.endDateTime!!.toInstant(TimeZone.currentSystemDefault()),
                            isAllDay = state.isAllDay,
                            location = state.location?.ifBlank { null }
                        )
                    )
                }
                ItemType.TASK -> TODO()
            }
        }
    }

    fun reset() {
        _state.update { AddBottomSheetContract.State() }
    }
}