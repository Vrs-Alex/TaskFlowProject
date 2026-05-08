package com.vrsalex.taskflow.presentation.feature.create_item

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.common.extension.getItemTypeColor
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventFields
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventOptFields
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskFields
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.chip.AppChipMenu
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun AddItemContent(
    viewModel: AddItemViewModel,
    onClose: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isVisible by viewModel.isVisibleState.collectAsStateWithLifecycle()

    BackHandler(enabled = isVisible) {
        onClose()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.scrim.copy(alpha = 0.5f))
                    .pointerInput(Unit) {
                        detectTapGestures { onClose() }
                    }
            )

            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) { it },
                exit = slideOutVertically(tween(300)) { it },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .align(Alignment.BottomCenter)
            ) {
                AddItemSheetContent(
                    state = state,
                    onAction = viewModel::onAction,
                    onClose = onClose,
                    onResume = viewModel.resumeGeneralSheet
                )
            }
        }
    }
}

@Composable
private fun AddItemSheetContent(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit,
    onClose: () -> Unit,
    onResume: Flow<Unit>
) {
    Column(Modifier.fillMaxWidth()) {
        ItemSelectorOverlay(
            visible = state.activeSelector != SelectorType.NONE,
            searchQuery = state.selectorSearch,
            onSearchQueryChange = { onAction(AddItemContract.Action.SelectorSearchChanged(it)) },
            onDismiss = { onAction(AddItemContract.Action.HideSelector) },
            onAddClick = { onAction(AddItemContract.Action.SelectorCreate(it)) },
            items = state.filteredSelectorItems,
            selectedIds = state.selectedSelectorIds,
            onItemClick = { item ->
                when (state.activeSelector) {
                    SelectorType.TAGS -> {
                        val tag = state.availableTags.first { it.id == item.id }
                        onAction(AddItemContract.Action.TagToggled(tag))
                    }
                    SelectorType.AREA -> {
                        val area = state.availableAreas.firstOrNull { it.id == item.id }
                        onAction(AddItemContract.Action.AreaChanged(area))
                    }
                    SelectorType.NONE -> {}
                }
            }
        )

        Box(
            Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.extraLarge)
                .background(AppTheme.colors.surface)
                .pointerInput(Unit) {
                    detectTapGestures { }
                }
        ) {
            AddItemBaseContent(state, onAction, onClose, onResume)
        }
    }
}

@Composable
private fun AddItemBaseContent(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit,
    onClose: () -> Unit = {},
    onResume: Flow<Unit>
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.activeSelector == SelectorType.NONE, state.type) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        onResume.collect {
            keyboardController?.show()
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BasicTextField(
                value = state.title,
                onValueChange = { onAction(AddItemContract.Action.TitleChanged(it)) },
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
                                text = stringResource(R.string.item_create_example),
                                style = AppTheme.types.titleLarge,
                                color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            IconButton(
                onClick = {
                    onAction(AddItemContract.Action.Save)
                    onClose()
                },
                shape = CircleShape,
                modifier = Modifier
                    .height(42.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.primary,
                    disabledContainerColor = AppTheme.colors.primary.copy(alpha = 0.4f)
                ),
                enabled = state.isActiveSubmit
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.up),
                    contentDescription = "Send",
                    tint = AppTheme.colors.onPrimary
                )
            }
        }

        SmallTextInput(
            value = state.description,
            onValueChange = { onAction(AddItemContract.Action.DescriptionChanged(it)) },
            placeholder = stringResource(R.string.description)
        )

        Spacer(Modifier.height(16.dp))

        LazyRow(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(contentType = "Type") {
                AppChipMenu(
                    selected = state.type,
                    items = ItemType.entries,
                    itemText = { it.name },
                    itemColor = { getItemTypeColor(it) },
                    onItemSelected = { onAction(AddItemContract.Action.TypeChanged(it)) }
                )
            }

            item(contentType = "SubFields") {
                AnimatedContent(
                    targetState = state.type,
                    transitionSpec = {
                        fadeIn(tween(200)) + expandHorizontally(tween(250)) togetherWith
                                fadeOut(tween(150)) + shrinkHorizontally (tween(200))
                    },
                    label = "type_fields"
                ) { type ->
                    when (type) {
                        ItemType.EVENT -> AddItemEventFields(state, onAction)
                        ItemType.TASK -> AddItemTaskFields(state, onAction)
                    }
                }
            }

            item(contentType = "Area") {
                AppChip(
                    text = state.selectedArea?.name ?: stringResource(R.string.area),
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = state.selectedArea != null,
                    onClick = { onAction(AddItemContract.Action.ShowSelector(SelectorType.AREA)) },
                    modifier = Modifier.animateItem()
                )
            }

            item(contentType = "Tags") {
                AppChip(
                    text = if (state.selectedTags.isEmpty()) { stringResource(R.string.tag) }
                    else { state.selectedTags.joinToString(separator = ", ") { it.name } },
                    color = AppTheme.colors.onSurfaceVariant,
                    filled = state.selectedTags.isNotEmpty(),
                    onClick = { onAction(AddItemContract.Action.ShowSelector(SelectorType.TAGS)) },
                    modifier = Modifier.animateItem()
                )
            }
        }

        AnimatedContent(
            targetState = state.type,
            transitionSpec = {
                fadeIn(tween(200)) + expandHorizontally(tween(250)) togetherWith
                        fadeOut(tween(150)) + shrinkHorizontally (tween(200))
            },
            label = "type_fields"
        ) { type ->
            when (type) {
                ItemType.EVENT -> AddItemEventOptFields(state, onAction)
                ItemType.TASK -> {}
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}


