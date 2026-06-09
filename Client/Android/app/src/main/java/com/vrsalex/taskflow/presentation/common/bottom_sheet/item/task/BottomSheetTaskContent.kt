package com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.common.model.toOptional
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetBaseContent
import com.vrsalex.taskflow.presentation.model.TaskUiModel
import com.vrsalex.taskflow.presentation.model.toUiModel
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.controller.checkbox.AppCheckbox
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.LocalDateTime


@Composable
fun BottomSheetTaskContent(
    taskUi: TaskUiModel,
    viewModel: TaskDetailViewModel,
    onClose: () -> Unit
) {

    ItemBottomSheetBaseContent(
        itemType = ItemCardType.Task,
        header = stringResource(R.string.task),
        title = taskUi.task.base.name,
        description = taskUi.task.base.description,
        area = taskUi.task.base.area?.toUiModel(),
        tags = taskUi.tags,
        synced = taskUi.task.isSynced,
        status = taskUi.task.base.status,
        subline = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.surfaceElevated,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppCheckbox(
                    checked = taskUi.isCompleted,
                    onToggle = { viewModel.changeMarkAsDone(it) }
                )
                Text(
                    text = stringResource(
                        if (taskUi.isCompleted) R.string.done
                        else R.string.un_done
                    ),
                    style = AppTheme.types.body,
                    color = AppTheme.colors.onSurfaceMuted
                )
                Spacer(Modifier.weight(1f))
                val date = LocalDateTime(taskUi.task.dueDate, taskUi.task.dueTime ?: return@Row)
                Text(
                    text = date.formatForChip(false),
                    style = AppTheme.types.body,
                    color = AppTheme.colors.onSurfaceMuted
                )
            }
        },
        onClose = onClose,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onDelete = {
            viewModel.delete(taskUi.task.id)
            onClose()
        },
        onArchive = { status ->
            viewModel.update(
                TaskUpdate(
                    base = ItemUpdate(
                        id = taskUi.task.id,
                        serverId = taskUi.task.serverId,
                        version = taskUi.task.version,
                        status = status.toOptional()
                    )
                )
            )
            onClose()
        },
        onSyncWithServer = {
            viewModel.syncItem(taskUi.task.id)
        }
    )
}