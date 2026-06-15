package com.vrsalex.taskflow.presentation.model

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.workspace.tag.Tag
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import kotlin.uuid.Uuid

/** Несёт доменный [tag] (нужен при сохранении) + готовые для UI поля. */
data class TagUiModel(
    val tag: Tag,
    val id: Uuid,
    val name: String,
    val color: Color,
)

fun Tag.toUiModel(): TagUiModel = TagUiModel(
    tag = this,
    id = syncModel.id,
    name = name.value,
    color = color.value.toComposeColor(),
)
