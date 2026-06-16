package com.vrsalex.taskflow.presentation.model.note

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.note.event.Event
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import com.vrsalex.taskflow.presentation.model.note.field.PriorityUi
import com.vrsalex.taskflow.presentation.model.note.field.toUi
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.Uuid

data class EventUiModel(
    val id: Uuid,
    val title: String,
    val time: String,
    val priority: PriorityUi?,
    val area: Pair<String, Color>?,
    val tags: List<Pair<String, Color?>>,
    val synced: Boolean,
)

fun Event.toUiModel(): EventUiModel {
    val tz = TimeZone.currentSystemDefault()
    val start = startDate.toLocalDateTime(tz).formatForChip(isAllDay)
    val end = endDate?.toLocalDateTime(tz)?.formatForChip(isAllDay)
    return EventUiModel(
        id = note.syncModel.id,
        title = note.name.value,
        time = if (end != null) "$start → $end" else start,
        priority = note.priority.toUi(),
        area = note.area?.let { it.name.value to it.color.value.toComposeColor() },
        tags = note.tags.map { it.name.value to it.color.value.toComposeColor() },
        synced = note.syncModel.isSynced,
    )
}
