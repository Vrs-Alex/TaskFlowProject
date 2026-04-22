package com.vrsalex.uikit.component.button

import android.R.attr.text
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    text: String,
    state: AppButtonState,
    modifier: Modifier = Modifier,
    leftIcon: @Composable (() -> Unit)? = null,
    rightIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {

    val borderColor by animateColorAsState(
        targetValue = if (enabled) AppTheme.colors.onSurface else AppTheme.colors.onSurface.copy(alpha = 0.5f)
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(state.height)
            .fillMaxWidth(),
        shape = AppTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            contentColor = AppTheme.colors.onSurface,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = AppTheme.colors.onSurface.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftIcon?.let { it() }
            Text(
                text = text,
                style = AppTheme.types.button,
                overflow = TextOverflow.Ellipsis
            )
            rightIcon?.let { it() }
        }
    }



}