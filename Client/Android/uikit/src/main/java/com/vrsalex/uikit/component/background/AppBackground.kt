package com.vrsalex.uikit.component.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.BackgroundDark
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun AppBackground(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 2.dp,
    padding: Dp = 4.dp,
    color: Color = AppTheme.colors.background.copy(alpha = 0.75f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .hazeEffect(state = hazeState) {
                blurEffect {
                    this.blurRadius = blurRadius
                    this.backgroundColor = color
                }
            }
            .background(color)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}