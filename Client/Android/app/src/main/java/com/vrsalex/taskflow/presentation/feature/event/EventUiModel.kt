package com.vrsalex.taskflow.presentation.feature.event


import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.utils.toComposeColor
import com.vrsalex.taskflow.presentation.common.extension.formatDateRange

data class EventUiModel(
    val event: Event,
    val dateString: String,
    val tags: List<Pair<String, Color>>,
    val areaColor: Color?
)


fun Event.toUiModel() = EventUiModel(
    event = this,
    dateString = formatDateRange(),
    tags = base.tags.mapNotNull { tag ->
        tag.color.toComposeColor()?.let {
            return@mapNotNull Pair(tag.name, it)
        }
        null
    },
    areaColor = base.area?.color?.toComposeColor()
)