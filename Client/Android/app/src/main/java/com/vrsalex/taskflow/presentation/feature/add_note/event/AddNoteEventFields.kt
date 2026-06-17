package com.vrsalex.taskflow.presentation.feature.add_note.event

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
fun AddNoteEventFields(
    state: AddItemEventContract.State,
    onAction: (AddItemEventContract.Action) -> Unit,
    onResumeSheet: () -> Unit,
) {
    val tz = TimeZone.currentSystemDefault()
    val now = remember { Clock.System.now().toLocalDateTime(tz) }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppChip(
            text = state.startDateTime?.formatForChip(state.isAllDay) ?: stringResource(R.string.start_date),
            color = getNoteTypeColor(NoteType.EVENT),
            filled = state.startDateTime != null,
            onClick = { showStartPicker = true }
        )
        AppChip(
            text = state.endDateTime?.formatForChip(state.isAllDay) ?: stringResource(R.string.end_date),
            color = getNoteTypeColor(NoteType.EVENT),
            filled = state.endDateTime != null,
            onClick = { showEndPicker = true }
        )
    }

    if (showStartPicker) {
        AppDateTimePicker(
            initial = state.startDateTime ?: now,
            title = stringResource(R.string.start_date),
            initialAllDay = state.isAllDay,
            onConfirm = { picked ->
                onAction(AddItemEventContract.Action.StartDateTimeChanged(
                    LocalDateTime(picked.date, picked.time ?: now.time)
                ))
                onAction(AddItemEventContract.Action.IsAllDayChanged(picked.time == null))
                onResumeSheet()
                showStartPicker = false
            },
            onDismiss = {
                onAction(AddItemEventContract.Action.StartDateTimeChanged(null))
                onResumeSheet()
                showStartPicker = false
            }
        )
    }

    if (showEndPicker) {
        AppDateTimePicker(
            initial = state.endDateTime ?: state.startDateTime ?: now,
            title = stringResource(R.string.end_date),
            initialAllDay = state.isAllDay,
            showTimeCheckbox = false,
            onConfirm = { picked ->
                onAction(AddItemEventContract.Action.EndDateTimeChanged(
                    LocalDateTime(picked.date, picked.time ?: now.time)
                ))
                onResumeSheet()
                showEndPicker = false
            },
            onDismiss = {
                onAction(AddItemEventContract.Action.EndDateTimeChanged(null))
                onResumeSheet()
                showEndPicker = false
            }
        )
    }
}
