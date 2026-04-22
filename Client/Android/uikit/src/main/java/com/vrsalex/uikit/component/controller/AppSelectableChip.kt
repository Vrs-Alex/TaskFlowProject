package com.vrsalex.uikit.component.controller

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun AppSelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = AppTheme.colors.primary,
    inactiveColor: Color = AppTheme.colors.onSurface.copy(alpha = 0.5f),
) {
    val borderColor = if (selected) activeColor else inactiveColor
    val textColor = if (selected) activeColor else inactiveColor
    val backgroundColor = if (selected) activeColor.copy(alpha = 0.12f) else Color.Transparent

    Surface(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 32.dp),
        shape = AppTheme.shapes.small,
        color = backgroundColor,
        contentColor = textColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AppTheme.types.label,
                color = textColor
            )
        }
    }
}