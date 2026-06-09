package com.vrsalex.taskflow.presentation.model

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.utils.toComposeColor
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import kotlin.uuid.Uuid

data class TagUiModel(
    val id: Uuid,
    val name: String,
    val color: Color
)

fun Tag.toUiModel() = TagUiModel(
    id = this.id,
    name = this.name,
    color = this.color.toComposeColor() ?: Color.Gray
)