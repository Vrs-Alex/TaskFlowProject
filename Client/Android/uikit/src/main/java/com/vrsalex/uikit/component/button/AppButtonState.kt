package com.vrsalex.uikit.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface AppButtonState {

    val height: Dp


    object Small: AppButtonState {
        override val height: Dp = 40.dp
    }

    object Medium: AppButtonState {
        override val height: Dp = 48.dp
    }

    object Large: AppButtonState {
        override val height: Dp = 52.dp
    }


}