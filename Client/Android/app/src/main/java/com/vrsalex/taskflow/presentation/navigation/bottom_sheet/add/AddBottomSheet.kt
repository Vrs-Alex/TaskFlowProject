package com.vrsalex.taskflow.presentation.navigation.bottom_sheet.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.chip.AppChipMenu
import com.vrsalex.uikit.component.controller.chip.AppMultiChipMenu
import com.vrsalex.uikit.component.modal.AppBottomSheet
import com.vrsalex.uikit.component.time.AppDateTimePicker
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.EventHue
import com.vrsalex.uikit.theme.TaskHue
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock


@Composable
fun AddBottomSheet(
    onClose: () -> Unit = {}
) {

    val viewModel: AddBottomSheetViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddBottomSheetContent(state, viewModel::onAction, onClose)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBottomSheetContent(
    state: AddBottomSheetContract.State,
    onAction: (AddBottomSheetContract.Action) -> Unit,
    onClose: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }


    AppBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onClose()
            }
        },
        sheetState = sheetState
    ) {
        Column(Modifier.fillMaxWidth()) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Заголовок
                BasicTextField(
                    value = state.title,
                    onValueChange = { onAction(AddBottomSheetContract.Action.TitleChanged(it)) },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    maxLines = 2,
                    cursorBrush = SolidColor(AppTheme.colors.primary),
                    textStyle = AppTheme.types.titleLarge.copy(color = AppTheme.colors.onSurface),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.TopStart,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            if (state.title.isEmpty()) {
                                Text(
                                    text = "Название...",
                                    style = AppTheme.types.titleLarge,
                                    color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                IconButton(
                    onClick = { onAction(AddBottomSheetContract.Action.Save) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.height(38.dp).width(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = AppTheme.colors.primary
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.send),
                        contentDescription = "Send"
                    )
                }
            }

            BasicTextField(
                value = state.description,
                onValueChange = { onAction(AddBottomSheetContract.Action.DescriptionChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                cursorBrush = SolidColor(AppTheme.colors.primary),
                textStyle = AppTheme.types.body.copy(color = AppTheme.colors.onSurface),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (state.description.isEmpty()) {
                            Text(
                                text = "Описание...",
                                style = AppTheme.types.body,
                                color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            LazyRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AppChipMenu(
                        selected = state.type,
                        items = ItemType.entries,
                        itemText = { it.name },
                        itemColor = { getTypeColor(it) },
                        onItemSelected = { onAction(AddBottomSheetContract.Action.TypeChanged(it)) }
                    )
                }

                item {
                    AppChipMenu(
                        selected = state.area,
                        items = listOf(null) + state.availableAreas,
                        itemText = { it?.name ?: "Область" },
                        itemColor = { it?.color ?: AppTheme.colors.onSurfaceVariant },
                        onItemSelected = { onAction(AddBottomSheetContract.Action.AreaChanged(it)) }
                    )
                }

                item {
                    AppMultiChipMenu(
                        selected = state.selectedTags,
                        items = state.availableTags,
                        itemText = { it.name },
                        itemColor = { it.color },
                        placeholder = "# Теги",
                        onItemToggled = { onAction(AddBottomSheetContract.Action.TagToggled(it)) }
                    )
                }
            }

            AnimatedContent(
                targetState = state.type,
                transitionSpec = {
                    fadeIn(tween(200)) + expandVertically(tween(250)) togetherWith
                            fadeOut(tween(150)) + shrinkVertically(tween(200))
                },
                label = "type_fields"
            ) { type ->
                when (type) {
                    ItemType.EVENT -> EventExtraFields(state, onAction)
                    ItemType.TASK -> EventExtraFields(state, onAction)
//                    ItemType.GOAL -> GoalExtraFields(state, onAction)
//                    ItemType.HABIT -> HabitExtraFields(state, onAction)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
@Composable
private fun EventExtraFields(
    state: AddBottomSheetContract.State,
    onAction: (AddBottomSheetContract.Action) -> Unit
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    val tz = TimeZone.currentSystemDefault()
    val now = remember { Clock.System.now().toLocalDateTime(tz) }

    AnimatedVisibility(
        visible = state.type == ItemType.EVENT,
        enter = fadeIn(tween(200)) + expandVertically(tween(250)),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppChip(
                    text = state.startDateTime?.formatForChip(state.isAllDay) ?: "Начало",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = state.startDateTime != null,
                    onClick = { showStartPicker = true }
                )
                AppChip(
                    text = state.endDateTime?.formatForChip(state.isAllDay) ?: "Конец",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = state.endDateTime != null,
                    onClick = { showEndPicker = true }
                )
                AppChip(
                    text = "Весь день",
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = state.isAllDay,
                    onClick = { onAction(AddBottomSheetContract.Action.IsAllDayChanged(!state.isAllDay)) }
                )
            }
        }
    }

    if (showStartPicker) {
        AppDateTimePicker(
            initial = state.startDateTime ?: now,
            title = "Начало",
            onConfirm = { dateTime ->
                onAction(AddBottomSheetContract.Action.StartDateTimeChanged(dateTime.dateTime))
                showStartPicker = false
            },
            onDismiss = { showStartPicker = false }
        )
    }

    if (showEndPicker) {
        AppDateTimePicker(
            initial = state.endDateTime ?: state.startDateTime ?: now,
            title = "Конец",
            onConfirm = { dateTime ->
                onAction(AddBottomSheetContract.Action.EndDateTimeChanged(dateTime.dateTime))
                showEndPicker = false
            },
            onDismiss = { showEndPicker = false }
        )
    }
}


@Composable
private fun getTypeColor(type: ItemType) = when(type){
    ItemType.EVENT -> EventHue
    ItemType.TASK -> TaskHue
}