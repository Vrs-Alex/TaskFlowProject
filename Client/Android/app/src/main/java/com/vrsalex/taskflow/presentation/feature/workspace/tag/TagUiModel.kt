package com.vrsalex.taskflow.presentation.feature.workspace.tag

import androidx.compose.ui.graphics.Color
import kotlin.uuid.Uuid

data class TagUiModel(
    val id: Uuid,
    val name: String,
    val color: Color
)
