package com.vrsalex.uikit.component.controller.progress


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun AppProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    backgroundColor: Color = AppTheme.colors.outline,
    height: Dp = 6.dp,
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor, RoundedCornerShape(height)),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .drawWithCache {
                    onDrawBehind {
                        drawRoundRect(
                            color = backgroundColor,
                            cornerRadius = CornerRadius(size.height / 2)
                        )
                        drawRoundRect(
                            color = color,
                            size = Size(size.width * animatedProgress.value, size.height),
                            cornerRadius = CornerRadius(size.height / 2)
                        )
                    }
                }
        )
    }
}