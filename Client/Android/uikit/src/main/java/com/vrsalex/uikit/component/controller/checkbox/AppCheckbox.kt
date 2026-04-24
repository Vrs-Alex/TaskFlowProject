package com.vrsalex.uikit.component.controller.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppCheckbox(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    val bg by animateColorAsState(
        if (checked) AppTheme.typeColors.task else Color.Transparent,
        label = "checkbox-bg",
    )
    val border by animateColorAsState(
        if (checked) AppTheme.typeColors.task else AppTheme.colors.outlineVariant,
        label = "checkbox-border",
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clickable { onToggle() }
            .background(bg, RoundedCornerShape(8.dp))
            .border(1.5.dp, border, RoundedCornerShape(8.dp)),
    ) {
        if (checked) {
            Icon(
                painter = painterResource(R.drawable.check),
                contentDescription = null,
                tint = AppTheme.colors.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}