package com.vrsalex.uikit.component.controller

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        thumbContent = {},
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = AppTheme.colors.onSurface,
            checkedTrackColor = AppTheme.colors.primary,
            uncheckedThumbColor = AppTheme.colors.onSecondary,
            uncheckedTrackColor = AppTheme.colors.secondary.copy(alpha = 0.1f),
            uncheckedBorderColor = AppTheme.colors.secondary.copy(alpha = 0.1f),
        )
    )

}