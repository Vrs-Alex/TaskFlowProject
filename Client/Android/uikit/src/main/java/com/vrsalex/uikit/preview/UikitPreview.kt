package com.vrsalex.uikit.preview

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import com.vrsalex.uikit.component.button.AppOutlinedButton
import com.vrsalex.uikit.component.controller.AppChip
import com.vrsalex.uikit.component.controller.AppSelectableChip
import com.vrsalex.uikit.component.controller.AppSwitch
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.AppPasswordInput
import com.vrsalex.uikit.component.input.AppTextInput
import com.vrsalex.uikit.component.modal.AppBottomSheet
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.TaskFlowTheme

@Composable
fun UikitPreview() {
    TaskFlowTheme() {
        Box(Modifier.fillMaxSize()) {
            var isVisible by remember { mutableStateOf(false) }

            Column(
                Modifier.fillMaxSize()
                    .background(AppTheme.colors.background)
                    .systemBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                AppButton(
                    onClick = { },
                    "Войти в аккаунт",
                    state = AppButtonState.Large,
                    rightIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error),
                            contentDescription = null
                        )
                    }
                )

                AppButton(
                    onClick = { },
                    "Войти в аккаунт",
                    state = AppButtonState.Medium,
                    enabled = false,
                    rightIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error),
                            contentDescription = null
                        )
                    }
                )

                AppOutlinedButton(
                    onClick = { },
                    "Войти в аккаунт",
                    state = AppButtonState.Large,
                    leftIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error),
                            contentDescription = null
                        )
                    }
                )

                AppOutlinedButton(
                    onClick = { },
                    "Войти в аккаунт",
                    state = AppButtonState.Large,
                    leftIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error),
                            contentDescription = null
                        )
                    },
                    enabled = false
                )

                AppOutlinedButton(
                    onClick = { },
                    "Войти в аккаунт",
                    state = AppButtonState.Small,
                    leftIcon = {
                        Icon(
                            painter = painterResource(R.drawable.error),
                            contentDescription = null
                        )
                    },
                    enabled = false,
                    modifier = Modifier.width(200.dp)
                )

                var text by remember { mutableStateOf("") }
                AppTextInput(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Username: ",
                    label = "Username",
                    error = if (text.length >= 10) "Username must be at least 10 characters long" else null,
                    leadingIcon = {
                        AppIcon(
                            icon = R.drawable.fire
                        )
                    }
                )


                var text1 by remember { mutableStateOf("") }
                AppPasswordInput(
                    value = text1,
                    onValueChange = { text1 = it },
                    placeholder = "Username: ",
                    label = "Username",
                    error = if (text.length >= 10) "Username must be at least 10 characters long" else null
                )

                var checked by remember { mutableStateOf(false) }
                AppSwitch(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )

                AppChip(
                    text = "Мероприятие",
                    color = Color.Yellow
                )

                AppChip(
                    text = "Мекзщлкезщрлзщкелрзщекзрщокзщорлкрозкщеоркзщерозщкоерзщокезро",
                    color = Color.Red,
                    onClick = { isVisible = true }
                )

                var selected by remember { mutableStateOf(false) }
                AppSelectableChip(
                    text = "With time",
                    selected = selected,
                    onClick = { selected = !selected }
                )

            }



        }
    }
}