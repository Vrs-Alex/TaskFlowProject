package com.vrsalex.taskflow.presentation.feature.create_item.event

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.time.AppDateTimePicker
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun AddItemEventFields(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit
) {
    val eventData = state.subItemData as? AddItemContract.SubItemData.Event

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val tz = TimeZone.currentSystemDefault()
    val now = remember { Clock.System.now().toLocalDateTime(tz) }

    AnimatedVisibility(
        visible = eventData != null,
        enter = fadeIn(tween(200)) + expandVertically(tween(250)),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
    ) {
        val data = eventData ?: return@AnimatedVisibility

        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppChip(
                    text = data.startDateTime?.formatForChip(data.isAllDay) ?: "Начало",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = data.startDateTime != null,
                    onClick = { showStartPicker = true }
                )
                AppChip(
                    text = data.endDateTime?.formatForChip(data.isAllDay) ?: "Конец",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = data.endDateTime != null,
                    onClick = { showEndPicker = true }
                )
                AppChip(
                    text = "Весь день",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = data.isAllDay,
                    onClick = {
                        onAction(AddItemContract.Action.EventAction.IsAllDayChanged(!data.isAllDay))
                    }
                )
            }
        }
    }

    if (showStartPicker) {
        AppDateTimePicker(
            initial = eventData?.startDateTime ?: now,
            title = "Начало",
            onConfirm = { dateTime ->
                onAction(AddItemContract.Action.EventAction.StartDateTimeChanged(dateTime.dateTime))
                showStartPicker = false
            },
            onDismiss = {
                onAction(AddItemContract.Action.EventAction.StartDateTimeChanged(null))
                showStartPicker = false
            }
        )
    }

    if (showEndPicker) {
        AppDateTimePicker(
            initial = eventData?.endDateTime ?: eventData?.startDateTime ?: now,
            title = "Конец",
            onConfirm = { dateTime ->
                onAction(AddItemContract.Action.EventAction.EndDateTimeChanged(dateTime.dateTime))
                showEndPicker = false
            },
            onDismiss = {
                onAction(AddItemContract.Action.EventAction.EndDateTimeChanged(null))
                showEndPicker = false
            }
        )
    }
}