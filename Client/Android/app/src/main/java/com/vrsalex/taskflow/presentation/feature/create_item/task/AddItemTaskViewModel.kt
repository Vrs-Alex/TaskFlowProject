package com.vrsalex.taskflow.presentation.feature.create_item.task

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddItemTaskViewModel {

    private val _state = MutableStateFlow(AddItemTaskContract.State())
    val state = _state.asStateFlow()

    fun onAction(action: AddItemTaskContract.Action) {
        _state.update { state ->
            when (action) {
                is AddItemTaskContract.Action.DueDateChanged -> state.copy(dueDate = action.date)
                is AddItemTaskContract.Action.TimeChanged -> state.copy(time = action.time)
            }
        }
    }

    fun reset() {
        _state.value = AddItemTaskContract.State()
    }
}
