package com.vrsalex.taskflow.presentation.feature.create_item.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.vrsalex.uikit.component.time.AppDateTimePicker
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun AddItemTaskFields(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit
) {
    val taskData = state.subItemData as? AddItemContract.SubItemData.Task

    var showDatePicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val tz = TimeZone.currentSystemDefault()
    val now = remember { Clock.System.now().toLocalDateTime(tz) }

    AnimatedVisibility(
        visible = taskData != null,
        enter = fadeIn(tween(200)) + expandVertically(tween(250)),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
    ) {
        val data = taskData ?: return@AnimatedVisibility

        Column(
            Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppChip(
                    text = data.dueDate?.toString() ?: stringResource(R.string.date),
                    color = getItemTypeColor(ItemType.TASK),
                    filled = data.dueDate != null,
                    onClick = { showDatePicker = true }
                )
            }
        }
    }

    if (showDatePicker) {
        AppDateTimePicker(
            initial = LocalDateTime(taskData?.dueDate ?: now.date, taskData?.time ?: now.time),
            title = stringResource(R.string.start_date),
            onConfirm = { dateTime ->
                onAction(AddItemContract.Action.TaskAction.DueDateChanged(dateTime.date))
                onAction(AddItemContract.Action.TaskAction.TimeChanged(dateTime.time))
                onAction(AddItemContract.Action.ResumeMainSheet)
                showDatePicker = false
            },
            onDismiss = {
                onAction(AddItemContract.Action.TaskAction.DueDateChanged(null))
                onAction(AddItemContract.Action.TaskAction.TimeChanged(null))
                onAction(AddItemContract.Action.ResumeMainSheet)
                showDatePicker = false
            }
        )
    }
}