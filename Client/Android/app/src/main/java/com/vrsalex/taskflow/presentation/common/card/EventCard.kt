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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.presentation.model.note.EventUiModel
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun EventCard(
    ui: EventUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ItemCard(
        modifier = modifier,
        type = ItemCardType.Event,
        title = ui.title,
        subline = {
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
                    text = ui.time,
                    style = AppTheme.types.label,
                    color = AppTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }
        },
        priority = ui.priority,
        area = ui.area,
        tags = ui.tags,
        synced = ui.synced,
        onClick = onClick,
    )
}
