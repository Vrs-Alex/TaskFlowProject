package com.vrsalex.uikit.extention

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun Modifier.glass(
    shape: Shape = AppTheme.shapes.medium,
    alpha: Float = 0.72f,
): Modifier = this
    .shadow(
        elevation = 10.dp,
        shape = shape,
        clip = false,
        ambientColor = Color.Black.copy(alpha = 0.35f),
        spotColor = Color.Black.copy(alpha = 0.5f),
    )
    .clip(shape)
    .background(AppTheme.colors.surface.copy(alpha = alpha))
    .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.02f)),
        ),
        shape = shape,
    )