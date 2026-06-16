package com.vrsalex.uikit.component.card

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun NoteCard(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    areaName: String? = null,
    areaColor: Color? = null,
    tags: List<Pair<String, Color?>> = emptyList(),
    synced: Boolean = true,
    onClick: () -> Unit = {},
) {
    ItemCard(
        modifier = modifier,
        type = ItemCardType.Note,
        title = title,
        subline = {
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = AppTheme.types.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        },
        areaName = areaName,
        areaColor = areaColor,
        tags = tags,
        synced = synced,
        onClick = onClick,
    )
}
