package com.vrsalex.uikit.component.tabbar

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme

@Composable
internal fun AppBottomTabBarItem(
    isSelected: Boolean,
    unSelectedIcon: Int,
    selectedIcon: Int,
    @StringRes titleId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    ) {
        Crossfade(isSelected) { selected ->
            if (selected){
                AppIcon(selectedIcon)
            } else AppIcon(unSelectedIcon)
        }
        Text(
            text = stringResource(titleId),
            style = AppTheme.types.caption,
            color = AppTheme.colors.onSurface,
            modifier = Modifier
                .graphicsLayer(
                    alpha = if (isSelected) 1f else 0.6f
                )
        )

    }

}