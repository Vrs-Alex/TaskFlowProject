package com.vrsalex.uikit.component.controller

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun AppChip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val contentColor = if (enabled) color else color.copy(alpha = 0.5f)
    val borderColor = if (enabled) color else color.copy(alpha = 0.5f)
    val backgroundColor = if (enabled) Color.Transparent else color.copy(alpha = 0.06f)

    Surface(
        onClick = onClick ?: {},
        enabled = enabled && onClick != null,
        modifier = modifier.defaultMinSize(minHeight = 32.dp)
            .sizeIn(maxHeight = 32.dp, maxWidth = 240.dp),
        shape = AppTheme.shapes.small,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AppTheme.types.label,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}