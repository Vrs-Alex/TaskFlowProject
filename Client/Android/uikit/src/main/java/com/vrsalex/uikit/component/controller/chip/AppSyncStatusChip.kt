package com.vrsalex.uikit.component.controller.chip

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppSyncStatusChip(
    isSynced: Boolean,
    modifier: Modifier = Modifier,
    counter: Int? = null,
    iconSynced: Int = R.drawable.cloud,
    iconPending: Int = R.drawable.sync,
) {
    val color = if (isSynced) AppTheme.colors.primary else AppTheme.colors.warning
    val bg = if (isSynced) AppTheme.colors.primarySoft
    else AppTheme.colors.warning.copy(alpha = 0.12f)


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .background(bg, AppTheme.shapes.round)
            .border(1.dp, color.copy(alpha = 0.35f), AppTheme.shapes.round)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        RotatingIcon(isAnimating = !isSynced, iconSynced, iconPending, color)

        Text(
            if (isSynced) "Синхрон." else "Не синхрон." + counter?.let { " ($it)" }.orEmpty(),
            style = AppTheme.types.label,
            color = color,
        )
    }
}

@Composable
private fun RotatingIcon(isAnimating: Boolean, iconSynced: Int, iconPending: Int, color: Color) {
    val rotation = if (isAnimating) {
        val infiniteTransition = rememberInfiniteTransition(label = "rotation")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3500)
            ),
            label = "rotation"
        ).value
    } else {
        0f
    }

    Icon(
        imageVector = if (!isAnimating) ImageVector.vectorResource(iconSynced) else ImageVector.vectorResource(
            iconPending
        ),
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(13.dp)
            .graphicsLayer {
                if (isAnimating) {
                    rotationZ = rotation
                }
            },
    )
}