package com.vrsalex.uikit.component.controller.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
    onClick: (() -> Unit)? = null,
    textStyle: TextStyle = AppTheme.types.label
) {
    val bg = if (filled) color.copy(alpha = 0.13f) else Color.Transparent
    val borderColor = color.copy(alpha = 0.44f)
    val textColor = if (filled) color else AppTheme.colors.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(AppTheme.shapes.small)
            .then(if (onClick != null) Modifier.clickable(indication = ripple(), interactionSource = null) { onClick() } else Modifier)
            .background(bg, AppTheme.shapes.small)
            .border(BorderStroke(1.dp, borderColor), AppTheme.shapes.small)
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        Text(text = text, style = textStyle, color = textColor)
    }
}