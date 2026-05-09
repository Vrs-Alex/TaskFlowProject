package com.vrsalex.taskflow.presentation.common.bottom_sheet.item.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.common.model.toOptional
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import com.vrsalex.taskflow.domain.item.task.TaskUpdate
import com.vrsalex.taskflow.presentation.common.bottom_sheet.item.ItemBottomSheetBaseContent
import com.vrsalex.taskflow.presentation.feature.task.TaskUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.area.toUiModel
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme


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
        }
    )
}