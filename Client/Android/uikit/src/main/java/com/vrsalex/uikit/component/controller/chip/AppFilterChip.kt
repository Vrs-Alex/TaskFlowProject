package com.vrsalex.uikit.component.controller.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg by animateColorAsState(
        if (selected) AppTheme.colors.primarySoft else Color.Transparent,
        label = "filter-bg",
    )
    val borderColor by animateColorAsState(
        if (selected) AppTheme.colors.primary else AppTheme.colors.outline,
        label = "filter-border",
    )
    val textColor by animateColorAsState(
        if (selected) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
        label = "filter-text",
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(bg, AppTheme.shapes.round)
            .border(BorderStroke(1.dp, borderColor), AppTheme.shapes.round)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(text = text, style = AppTheme.types.label, color = textColor)
    }
}