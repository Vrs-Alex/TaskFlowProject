package com.vrsalex.taskflow.presentation.common.extension

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(): Color = Color(removePrefix("#").toLong(16) or 0xFF000000)
