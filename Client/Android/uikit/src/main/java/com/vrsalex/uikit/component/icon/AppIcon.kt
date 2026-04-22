package com.vrsalex.uikit.component.icon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppIcon(
    icon: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    tint: Color = AppTheme.colors.onSurface
){

    val modifierClickable = if (onClick != null) {
        modifier
            .clip(CircleShape)
            .clickable(onClick = onClick, indication = ripple(), interactionSource = null)
    } else {
        modifier
    }

    Icon(
        painter = painterResource(icon),
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(24.dp).then(modifierClickable)
    )

}