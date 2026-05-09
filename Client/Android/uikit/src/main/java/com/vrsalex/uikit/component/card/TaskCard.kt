package com.vrsalex.uikit.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.checkbox.AppCheckbox
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun TaskCard(
    title: String,
    dueDate: String?,
    time: String?,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    isRecurring: Boolean = false,
    areaName: String? = null,
    areaColor: Color? = null,
    tags: List<Pair<String, Color?>> = emptyList(),
    synced: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    ItemCard(
        modifier = modifier.graphicsLayer {
            alpha = if (isCompleted) 0.8f
            else 1f
        },
        type = ItemCardType.Task,
        title = title,
        titleContent = {
            Text(
                title,
                style = AppTheme.types.title,
                color = AppTheme.colors.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                overflow = TextOverflow.Ellipsis
            )
        },
        subline = {
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                dueDate?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = dueDate,
                        style = AppTheme.types.label,
                        color = AppTheme.colors.onSurface.copy(alpha = 0.7f),
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
                if (isRecurring) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.time),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = AppTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        },
        areaName = areaName,
        areaColor = areaColor,
        tags = tags,
        synced = synced,
        action = {
            AppCheckbox(
                checked = isCompleted,
                onToggle = onCheckedChange
            )
        },
        onClick = onClick
    )
}
