package com.vrsalex.taskflow.presentation.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun getNoteTypeColor(type: NoteType): Color = when (type) {
    NoteType.TASK -> AppTheme.typeColors.task
    NoteType.EVENT -> AppTheme.typeColors.event
    else -> AppTheme.colors.onSurfaceVariant
}
