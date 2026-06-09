package com.vrsalex.taskflow.presentation.feature.add_item.components

import androidx.compose.ui.graphics.Color
import kotlin.uuid.Uuid

enum class SelectorType { TAGS, AREA, NONE }

data class SelectorItem(
    val id: Uuid,
    val name: String,
    val color: Color
)