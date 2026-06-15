package com.vrsalex.taskflow.presentation.common.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.domain.common.ui_messages.AppMessage
import com.vrsalex.taskflow.domain.common.ui_messages.MessageType
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppSnackBar(
    message: AppMessage.Notification,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    
    val (backgroundColor, contentColor, icon) = when (message.type) {
        MessageType.Success -> Triple(
            AppTheme.colors.primary,
            AppTheme.colors.onPrimary,
            R.drawable.fire
        )
        MessageType.ERROR -> Triple(
            AppTheme.colors.error,
            AppTheme.colors.onSurface,
            R.drawable.error
        )
        MessageType.INFO -> Triple(
            AppTheme.colors.warning,
            AppTheme.colors.onBackground,
            R.drawable.info
        )
    }

    Surface(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .imePadding()
            .fillMaxWidth(),
        shape = AppTheme.shapes.medium,
        color = backgroundColor,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = message.message,
                style = AppTheme.types.body,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { onClose() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "Close",
                    tint = contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}