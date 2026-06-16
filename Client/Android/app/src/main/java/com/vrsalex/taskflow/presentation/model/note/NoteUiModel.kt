package com.vrsalex.taskflow.presentation.model.note

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import com.vrsalex.taskflow.presentation.model.note.field.PriorityUi
import com.vrsalex.taskflow.presentation.model.note.field.toUi
import kotlin.uuid.Uuid

data class NoteUiModel(
    val id: Uuid,
    val title: String,
    val description: String?,
    val priority: PriorityUi?,
    val area: Pair<String, Color>?,
    val tags: List<Pair<String, Color?>>,
    val synced: Boolean,
)

fun Note.toUiModel(): NoteUiModel = NoteUiModel(
    id = syncModel.id,
    title = name.value,
    description = description?.value,
    priority = priority.toUi(),
    area = area?.let { it.name.value to it.color.value.toComposeColor() },
    tags = tags.map { it.name.value to it.color.value.toComposeColor() },
    synced = syncModel.isSynced,
)
