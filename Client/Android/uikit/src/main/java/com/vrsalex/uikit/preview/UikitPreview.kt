package com.vrsalex.uikit.preview

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import com.vrsalex.uikit.component.button.AppOutlinedButton
import com.vrsalex.uikit.component.controller.switch.AppSwitch
import com.vrsalex.uikit.component.controller.checkbox.AppCheckbox
import com.vrsalex.uikit.component.controller.checkbox.AppHabitToggle
import com.vrsalex.uikit.component.controller.chip.AppFilterChip
import com.vrsalex.uikit.component.controller.chip.AppFilterChipRow
import com.vrsalex.uikit.component.controller.chip.AppConnectedIndicator
import com.vrsalex.uikit.component.controller.progress.AppProgressBar
import com.vrsalex.uikit.component.controller.tab.AppSegmentedTabs
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.AppPasswordInput
import com.vrsalex.uikit.component.input.AppTextInput
import com.vrsalex.uikit.component.section.AppSectionHeader
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.EventHue
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

                var selected by remember { mutableStateOf(false) }

                AppFilterChip(
                    text = "Мероприятия",
                    selected = selected,
                    onClick = { selected = !selected }
                )

                val chips = listOf("Все заметки", "Теги", "Области", "Задачи")
                var selectedChip by remember { mutableStateOf(chips[0]) }
                AppFilterChipRow(
                    items = chips.map { Pair(it, it) },
                    selectedId = selectedChip,
                    onSelect = { selectedChip = it }
                )

                AppSegmentedTabs(
                    items = chips.map { Pair(it, it) },
                    selectedId = selectedChip,
                    onSelect = { selectedChip = it }
                )

                var isChecked by remember { mutableStateOf(false) }
                AppCheckbox(
                    checked = isChecked,
                    onToggle = { isChecked = !isChecked }
                )

                var isDone by remember { mutableStateOf(false) }
                var progress by remember { mutableStateOf(0f) }
                AppHabitToggle(
                    done = isDone,
                    onToggle = {
                        isDone = !isDone
                        progress = if (isDone) 0.75f else 0.2f
                    }
                )

                AppProgressBar(
                    progress = progress,
                )

                AppConnectedIndicator(
                    connect = isChecked
                )

                AppSectionHeader(
                    title = "мероприятия",
                    count = 3,
                    accentColor = EventHue
                )

                Spacer(Modifier.height(300.dp))

            }



        }
    }
}