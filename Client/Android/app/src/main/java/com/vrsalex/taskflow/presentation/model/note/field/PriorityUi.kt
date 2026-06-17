package com.vrsalex.taskflow.presentation.model.note.field

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class PriorityUi(
    @StringRes val titleRes: Int,
    val color: Color,
)
