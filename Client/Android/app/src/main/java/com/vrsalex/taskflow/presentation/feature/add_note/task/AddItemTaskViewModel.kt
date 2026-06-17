package com.vrsalex.taskflow.presentation.feature.add_note.task

import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddItemTaskViewModel {

    private val _state = MutableStateFlow(AddItemTaskContract.State())
    val state = _state.asStateFlow()

    fun onAction(action: AddItemTaskContract.Action) {
        _state.update { state ->
            when (action) {
                is AddItemTaskContract.Action.DueDateChanged -> state.copy(
                    dueDate = action.date,
                    // Повтор завязан на дату — при сбросе даты сбрасываем и повтор.
                    recurrenceType = if (action.date == null) null else state.recurrenceType,
                    recurrenceDays = if (action.date == null) emptySet() else state.recurrenceDays,
                )

                is AddItemTaskContract.Action.TimeChanged -> state.copy(time = action.time)

                is AddItemTaskContract.Action.RecurrenceTypeChanged -> state.copy(
                    recurrenceType = action.type,
                    // Дни нужны только для WEEKLY — для остальных типов очищаем.
                    recurrenceDays = if (action.type == RecurrenceType.WEEKLY) state.recurrenceDays else emptySet(),
                )

                is AddItemTaskContract.Action.RecurrenceDayToggled -> state.copy(
                    recurrenceDays = if (action.day in state.recurrenceDays)
                        state.recurrenceDays - action.day
                    else
                        state.recurrenceDays + action.day
                )
            }
        }
    }

    fun reset() {
        _state.value = AddItemTaskContract.State()
    }
}
