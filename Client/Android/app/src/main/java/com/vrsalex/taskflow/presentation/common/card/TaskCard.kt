package com.vrsalex.taskflow.presentation.common.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.presentation.model.note.TaskUiModel
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.checkbox.AppCheckbox
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun TaskCard(
    ui: TaskUiModel,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {},
) {
    ItemCard(
        modifier = modifier.graphicsLayer { alpha = if (ui.isCompleted) 0.6f else 1f },
        type = ItemCardType.Task,
        title = ui.title,
        titleContent = {
            Text(
                ui.title,
                style = AppTheme.types.title,
                color = AppTheme.colors.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                textDecoration = if (ui.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                overflow = TextOverflow.Ellipsis,
            )
        },
        subline = {
            ui.time?.let { time ->
                Row(
                    Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.time),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppTheme.colors.onSurface.copy(alpha = 0.7f),
                    )
                    Text(
                        text = time,
                        style = AppTheme.types.label,
                        color = AppTheme.colors.onSurface.copy(alpha = 0.7f),
                    )
                }
            }
        },
        priority = ui.priority,
        area = ui.area,
        tags = ui.tags,
        synced = ui.synced,
        action = {
            AppCheckbox(checked = ui.isCompleted, onToggle = onCheckedChange)
        },
        onClick = onClick,
    )
}
