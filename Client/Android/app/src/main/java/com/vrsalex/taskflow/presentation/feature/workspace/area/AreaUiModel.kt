package com.vrsalex.taskflow.presentation.feature.workspace.area

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.utils.toComposeColor
import com.vrsalex.taskflow.domain.workscape.area.Area
import kotlin.uuid.Uuid

data class AreaUiModel(
    val id: Uuid,
    val name: String,
    val color: Color
)

fun Area.toUiModel() = AreaUiModel(
    id = this.id,
    name = this.name,
    color = this.color.toComposeColor() ?: Color.Gray
)