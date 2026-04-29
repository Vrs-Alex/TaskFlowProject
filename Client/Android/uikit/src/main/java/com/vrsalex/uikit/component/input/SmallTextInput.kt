package com.vrsalex.uikit.component.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun SmallTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        maxLines = 3,
        cursorBrush = SolidColor(AppTheme.colors.primary),
        textStyle = AppTheme.types.body.copy(color = AppTheme.colors.onSurface),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.TopStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = AppTheme.types.body,
                        color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
                innerTextField()
            }
        }
    )

}