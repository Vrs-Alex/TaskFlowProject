package com.vrsalex.taskflow.presentation.model.note

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.note.task.Task
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import com.vrsalex.taskflow.presentation.model.note.field.PriorityUi
import com.vrsalex.taskflow.presentation.model.note.field.toUi
import kotlin.uuid.Uuid

data class TaskUiModel(
    val id: Uuid,
    val title: String,
    val time: String?,
    val isCompleted: Boolean,
    val priority: PriorityUi?,
    val area: Pair<String, Color>?,
    val tags: List<Pair<String, Color?>>,
    val synced: Boolean,
)

fun Task.toUiModel(): TaskUiModel = TaskUiModel(
    id = note.syncModel.id,
    title = note.name.value,
    time = dueTime?.toString(),
    isCompleted = isCompleted,
    priority = note.priority.toUi(),
    area = note.area?.let { it.name.value to it.color.value.toComposeColor() },
    tags = note.tags.map { it.name.value to it.color.value.toComposeColor() },
    synced = note.syncModel.isSynced,
)
