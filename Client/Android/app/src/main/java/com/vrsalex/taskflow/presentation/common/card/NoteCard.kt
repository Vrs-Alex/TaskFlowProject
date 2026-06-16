package com.vrsalex.taskflow.presentation.common.card

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.presentation.model.note.NoteUiModel
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun NoteCard(
    ui: NoteUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ItemCard(
        modifier = modifier,
        type = null,
        title = ui.title,
        subline = {
            if (!ui.description.isNullOrBlank()) {
                Text(
                    text = ui.description,
                    style = AppTheme.types.bodyMedium,
                    color = AppTheme.colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
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
