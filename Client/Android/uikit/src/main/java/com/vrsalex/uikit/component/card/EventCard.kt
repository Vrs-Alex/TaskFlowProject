package com.vrsalex.uikit.component.card

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun EventCard(
    title: String,
    time: String,
    modifier: Modifier = Modifier,
    areaName: String? = null,
    areaColor: Color? = null,
    tags: List<Pair<String, Color?>> = emptyList(),
    synced: Boolean = true,
    onClick: () -> Unit = {}
) {

    ItemCard(
        modifier = modifier,
        type = ItemCardType.Event,
        title = title,
        subline = {
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.time),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AppTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = time,
                    style = AppTheme.types.label,
                    color = AppTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        typeIcon = ImageVector.vectorResource(R.drawable.event),
        areaName = areaName,
        areaColor = areaColor,
        tags = tags,
        synced = synced,
        onClick = onClick
    )

}