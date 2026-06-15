package com.vrsalex.taskflow.presentation.model

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.domain.workspace.area.Area
import com.vrsalex.taskflow.presentation.common.extension.toComposeColor
import kotlin.uuid.Uuid

data class AreaUiModel(
    val area: Area,
    val id: Uuid,
    val name: String,
    val color: Color,
)

fun Area.toUiModel(): AreaUiModel = AreaUiModel(
    area = this,
    id = syncModel.id,
    name = name.value,
    color = color.value.toComposeColor(),
)
