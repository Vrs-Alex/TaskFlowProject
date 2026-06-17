package com.vrsalex.uikit.component.controller.chip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppConnectedIndicator(
    connect: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = !connect,
        enter = fadeIn(tween(500)),
        exit = fadeOut(tween(750)),
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        SyncRotatingIcon(color = AppTheme.colors.onSurfaceVariant)
    }
}

@Composable
private fun SyncRotatingIcon(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "sync_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "sync_rotation"
    )
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.sync),
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(14.dp)
            .graphicsLayer { rotationZ = rotation },
    )
}