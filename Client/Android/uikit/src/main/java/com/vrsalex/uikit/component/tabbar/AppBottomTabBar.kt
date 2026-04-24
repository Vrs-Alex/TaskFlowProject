package com.vrsalex.uikit.component.tabbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun <T : Any> AppBottomTabBar(
    tabs: List<AppBottomTabItem<T>>,
    isSelected: (T) -> Boolean,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val pureColor = AppTheme.colors.background

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .drawBehind {
                val brush = Brush.verticalGradient(
                    colors = listOf(pureColor.copy(alpha = 0.85f), pureColor),
                    startY = 0f,
                    endY = size.height
                )
                drawRect(brush = brush)
            }
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            tabs.forEach { item ->
                val selected = isSelected(item.payload)
                AppBottomTabBarItem(
                    isSelected = selected,
                    unSelectedIcon = item.unSelectedIcon,
                    selectedIcon = item.selectedIcon,
                    titleId = item.titleId,
                    onClick = { onSelected(item.payload) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}