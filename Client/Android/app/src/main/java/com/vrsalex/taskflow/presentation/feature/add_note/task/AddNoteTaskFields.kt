package com.vrsalex.taskflow.presentation.feature.add_note.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.extension.getNoteTypeColor
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.time.AppDateTimePicker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun AddNoteTaskFields(
    state: AddItemTaskContract.State,
    onAction: (AddItemTaskContract.Action) -> Unit,
    onResumeSheet: () -> Unit,
) {
    val tz = TimeZone.currentSystemDefault()
    val now = remember { Clock.System.now().toLocalDateTime(tz) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showRecurrenceSheet by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppChip(
            text = state.dueDate?.let {
                LocalDateTime(it, state.time ?: now.time).formatForChip(state.time == null)
            } ?: stringResource(R.string.date),
            color = getNoteTypeColor(NoteType.TASK),
            filled = state.dueDate != null,
            onClick = { showDatePicker = true }
        )

        if (state.dueDate != null) {
            AppChip(
                text = stringResource(state.recurrenceType.labelRes()),
                color = getNoteTypeColor(NoteType.TASK),
                filled = state.recurrenceType != null,
                onClick = { showRecurrenceSheet = true }
            )
        }
    }

    if (showDatePicker) {
        AppDateTimePicker(
            initial = LocalDateTime(state.dueDate ?: now.date, state.time ?: now.time),
            title = stringResource(R.string.date),
            initialAllDay = state.time == null && state.dueDate != null,
            onConfirm = { picked ->
                onAction(AddItemTaskContract.Action.DueDateChanged(picked.date))
                onAction(AddItemTaskContract.Action.TimeChanged(picked.time))
                onResumeSheet()
                showDatePicker = false
            },
            onDismiss = {
                onAction(AddItemTaskContract.Action.DueDateChanged(null))
                onAction(AddItemTaskContract.Action.TimeChanged(null))
                onResumeSheet()
                showDatePicker = false
            }
        )
    }

    if (showRecurrenceSheet) {
        RecurrenceBottomSheet(
            selectedType = state.recurrenceType,
            selectedDays = state.recurrenceDays,
            onTypeChange = { onAction(AddItemTaskContract.Action.RecurrenceTypeChanged(it)) },
            onDayToggle = { onAction(AddItemTaskContract.Action.RecurrenceDayToggled(it)) },
            onDismiss = {
                onResumeSheet()
                showRecurrenceSheet = false
            }
        )
    }
}
