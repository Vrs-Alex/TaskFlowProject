package com.vrsalex.uikit.component.tabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.component.background.AppBlurBackground
import com.vrsalex.uikit.theme.AppTheme
import dev.chrisbanes.haze.HazeState

@Composable
private fun tabBarShape() = AppTheme.shapes.extraLarge

@Composable
fun <T : Any> AppBottomTabBar(
    hazeState: HazeState,
    tabs: List<AppBottomTabItem<T>>,
    isSelected: (T) -> Boolean,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val surfaceColor = AppTheme.colors.surfaceElevated

    AppBlurBackground(
        hazeState = hazeState,
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 18.dp).padding(bottom = 8.dp)
            .dropShadow(
                shape = tabBarShape(),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.35f),
                    offset = DpOffset(x = 0.dp, y = 4.dp),
                    radius = 12.dp,
                    spread = 0.dp
                ),
            )
            .clip(tabBarShape())
            .background(surfaceColor),
        padding = 0.dp,
        color = surfaceColor.copy(alpha = 0.8f)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            tabs.forEach { item ->
                AppBottomTabBarItem(
                    isSelected = isSelected(item.payload),
                    unSelectedIcon = item.unSelectedIcon,
                    selectedIcon = item.selectedIcon,
                    titleId = item.titleId,
                    onClick = { onSelected(item.payload) },
                    modifier = Modifier.widthIn(min = 72.dp)
                )
            }
        }
    }
}
