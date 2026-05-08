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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.extension.getItemTypeColor
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContract
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.component.time.AppDateTimePicker
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.LocalDateTime
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
            Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppChip(
                    text = data.startDateTime?.formatForChip(data.isAllDay) ?: stringResource(R.string.start_date),
                    color = getItemTypeColor(ItemType.EVENT),
                    filled = data.startDateTime != null,
                    onClick = { showStartPicker = true }
                )
                AppChip(
                    text = data.endDateTime?.formatForChip(data.isAllDay) ?: stringResource(R.string.end_date),
                    color = getItemTypeColor(ItemType.EVENT),
                    filled = data.endDateTime != null,
                    onClick = { showEndPicker = true }
                )
            }
        }
    }

    if (showStartPicker) {
        AppDateTimePicker(
            initial = eventData?.startDateTime ?: now,
            title = stringResource(R.string.start_date),
            initialAllDay = eventData?.isAllDay == true,
            onConfirm = { dateTime ->
                onAction(AddItemContract.Action.EventAction.StartDateTimeChanged(
                    LocalDateTime(dateTime.date, dateTime.time ?: now.time))
                )
                onAction(AddItemContract.Action.EventAction.IsAllDayChanged(dateTime.time == null))
                onAction(AddItemContract.Action.ResumeMainSheet)
                showStartPicker = false
            },
            onDismiss = {
                onAction(AddItemContract.Action.EventAction.StartDateTimeChanged(null))
                onAction(AddItemContract.Action.ResumeMainSheet)
                showStartPicker = false
            }
        )
    }

    if (showEndPicker) {
        AppDateTimePicker(
            initial = eventData?.endDateTime ?: eventData?.startDateTime ?: now,
            title = stringResource(R.string.end_date),
            withTime = eventData?.isAllDay == false,
            enabledAllDay = false,
            onConfirm = { dateTime ->
                onAction(AddItemContract.Action.EventAction.EndDateTimeChanged(
                    LocalDateTime(dateTime.date, dateTime.time ?: now.time))
                )
                onAction(AddItemContract.Action.ResumeMainSheet)
                showEndPicker = false
            },
            onDismiss = {
                onAction(AddItemContract.Action.EventAction.EndDateTimeChanged(null))
                onAction(AddItemContract.Action.ResumeMainSheet)
                showEndPicker = false
            }
        )
    }
}