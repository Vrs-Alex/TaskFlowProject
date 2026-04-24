package com.vrsalex.taskflow.domain.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.toComposeColor(): Color? = try {
        Color(this.toColorInt())
    } catch (e: Exception) { null }