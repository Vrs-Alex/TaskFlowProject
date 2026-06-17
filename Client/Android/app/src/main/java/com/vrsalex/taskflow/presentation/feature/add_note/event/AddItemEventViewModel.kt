package com.vrsalex.taskflow.presentation.feature.add_note.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddItemEventViewModel {

    private val _state = MutableStateFlow(AddItemEventContract.State())
    val state = _state.asStateFlow()

    fun onAction(action: AddItemEventContract.Action) {
        _state.update { state ->
            when (action) {
                is AddItemEventContract.Action.StartDateTimeChanged -> state.copy(startDateTime = action.dateTime)
                is AddItemEventContract.Action.EndDateTimeChanged -> state.copy(endDateTime = action.dateTime)
                is AddItemEventContract.Action.IsAllDayChanged -> state.copy(isAllDay = action.isAllDay)
                is AddItemEventContract.Action.LocationChanged -> state.copy(location = action.location)
            }
        }
    }

    fun reset() {
        _state.value = AddItemEventContract.State()
    }
}
