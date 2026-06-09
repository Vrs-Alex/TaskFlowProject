package com.vrsalex.taskflow.presentation.model

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.item.task.Task
import com.vrsalex.taskflow.domain.item.task.TaskLogCreate
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.domain.utils.toComposeColor
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Clock
import kotlin.uuid.Uuid

data class TaskUiModel(
    val task: Task,
    val dueDateString: String,
    val isCompleted: Boolean = false,
    val completedLogId: Uuid? = null,
    val tags: List<Pair<String, Color>>,
    val areaColor: Color?
)

fun Task.toUiModel() = TaskUiModel(
    task = this,
    dueDateString = LocalDateTime(date = dueDate, time = dueTime ?: LocalTime(0, 0)).formatForChip(dueTime == null),
    isCompleted = completedLogs.isNotEmpty(),
    completedLogId = completedLogs.firstOrNull()?.id,
    tags = base.tags.mapNotNull { tag ->
        tag.color.toComposeColor()?.let { Pair(tag.name, it) }
    },
    areaColor = base.area?.color?.toComposeColor()
)

fun TaskUiModel.toTaskLog(forDate: LocalDate) = TaskLogCreate(
    id = completedLogId ?: Uuid.random(),
    taskId = task.base.id,
    date = forDate,
    completedAt = Clock.System.now()
)