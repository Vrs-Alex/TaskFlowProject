package com.vrsalex.uikit.component.controller.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppHabitToggle(
    done: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habitColor = AppTheme.typeColors.habit
    val bg by animateColorAsState(
        if (done) habitColor.copy(alpha = 0.13f) else Color.Transparent
    )
    val border by animateColorAsState(
        if (done) habitColor else AppTheme.colors.outlineVariant
    )
    val textColor by animateColorAsState(
        if (done) habitColor else AppTheme.colors.onSurfaceVariant
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(28.dp)
            .clickable { onToggle() }
            .background(bg, CircleShape)
            .border(1.5.dp, border, CircleShape),
    ) {
        Text(
            if (done) "✓" else "",
            style = AppTheme.types.label,
            color = textColor,
        )
    }
}