package com.vrsalex.taskflow.presentation.feature.item.note

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.item.note.Note
import com.vrsalex.taskflow.domain.utils.toComposeColor

data class NoteUiModel(
    val note: Note,
    val tags: List<Pair<String, Color>>,
    val areaColor: Color?
)


fun Note.toUiModel(isOnlyEnd: Boolean = false) = NoteUiModel(
    note = this,
    tags = tags.mapNotNull { tag ->
        tag.color.toComposeColor()?.let {
            return@mapNotNull Pair(tag.name, it)
        }
        null
    },
    areaColor = area?.color?.toComposeColor()
)