package com.vrsalex.uikit.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.TaskFlowTheme

@Composable
fun AppPasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    passwordTransformation: VisualTransformation = PasswordVisualTransformation('•'),
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    var isFocused by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            error != null -> AppTheme.colors.error
            isFocused -> AppTheme.colors.primary
            else -> AppTheme.colors.secondary.copy(alpha = 0.2f)
        }
    )

    val containerColor = when {
        error != null -> AppTheme.colors.error.copy(alpha = 0.05f)
        isFocused -> AppTheme.colors.surface
        else -> AppTheme.colors.secondary.copy(alpha = 0.05f)
    }

    val visualTransformation = if (isPasswordVisible) VisualTransformation.None else passwordTransformation


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        label?.let {
            Text(
                text = it,
                style = AppTheme.types.label,
                color = AppTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = AppTheme.types.body.copy(color = AppTheme.colors.onSurface),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            enabled = enabled,
            singleLine = singleLine,
            cursorBrush = SolidColor(AppTheme.colors.primary),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .background(
                            color = containerColor,
                            shape = AppTheme.shapes.medium
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = AppTheme.shapes.medium
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = AppTheme.types.body,
                                color = AppTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                        }
                        innerTextField()
                    }

                    if (value.isNotBlank()){
                        val icon = if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off
                        AppIcon(
                            icon = icon,
                            onClick = {
                                isPasswordVisible = !isPasswordVisible
                            }
                        )
                    }
                }
            }
        )

        if (error != null) {
            Text(
                text = error,
                style = AppTheme.types.label.copy(fontSize = 12.sp),
                color = AppTheme.colors.error,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AppPasswordInputPreview() {
    TaskFlowTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(AppTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppPasswordInput(
                value = "",
                onValueChange = {},
                label = "Email Address",
                placeholder = "example@mail.com"
            )
            AppPasswordInput(
                value = "StrongPassword123",
                onValueChange = {},
                label = "Password",
                placeholder = "Enter password",
                error = "Password is too weak"
            )
        }
    }
}
