package com.vrsalex.taskflow.presentation.feature.create_item

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventFields
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.chip.AppChipMenu
import com.vrsalex.uikit.component.controller.chip.AppMultiChipMenu
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.EventHue
import com.vrsalex.uikit.theme.TaskHue
import kotlin.uuid.Uuid

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
                    onClose = onClose
                )
            }
        }
    }
}

@Composable
private fun AddItemSheetContent(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit,
    onClose: () -> Unit
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
            AddItemBaseContent(state, onAction, onClose)
        }
    }
}

@Composable
private fun AddItemBaseContent(
    state: AddItemContract.State,
    onAction: (AddItemContract.Action) -> Unit,
    onClose: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.activeSelector == SelectorType.NONE) {
        focusRequester.requestFocus()
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
                onClick = {
                    onAction(AddItemContract.Action.Save)
                    onClose()
                },
                shape = CircleShape,
                modifier = Modifier
                    .height(42.dp)
                    .graphicsLayer {
                        alpha = if (state.title.isEmpty()) 0.4f else 1f
                    },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.primary
                )
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
            item {
                AppChipMenu(
                    selected = state.type,
                    items = ItemType.entries,
                    itemText = { it.name },
                    itemColor = { getTypeColor(it) },
                    onItemSelected = { onAction(AddItemContract.Action.TypeChanged(it)) }
                )
            }

            item {
                AppChip(
                    text = state.selectedArea?.name ?: "Область",
                    color = state.selectedArea?.color ?: AppTheme.colors.onSurface,
                    filled = state.activeSelector == SelectorType.AREA,
                    onClick = { onAction(AddItemContract.Action.ShowSelector(SelectorType.AREA)) },
                    modifier = Modifier.animateItem()
                )
            }

            item {
                AppChip(
                    text = state.selectedTags.joinToString(separator = ", ") { it.name } ?: "Область",
                    color = AppTheme.colors.onSurface,
                    filled = state.activeSelector == SelectorType.TAGS,
                    onClick = { onAction(AddItemContract.Action.ShowSelector(SelectorType.TAGS)) },
                    modifier = Modifier.animateItem()
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
                ItemType.EVENT -> AddItemEventFields(state, onAction)
                ItemType.TASK -> AddItemEventFields(state, onAction)
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun getTypeColor(type: ItemType) = when (type) {
    ItemType.EVENT -> EventHue
    ItemType.TASK -> TaskHue
}

@Composable
fun ItemSelectorOverlay(
    visible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddClick: (name: String) -> Unit,
    items: List<SelectorItem>,
    selectedIds: Set<Uuid>,
    onItemClick: (SelectorItem) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + expandVertically(tween(250), expandFrom = Alignment.Bottom),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200), shrinkTowards = Alignment.Bottom)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppTheme.colors.surface)
                .pointerInput(Unit) { detectTapGestures { } }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(AppTheme.colors.surfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    cursorBrush = SolidColor(AppTheme.colors.primary),
                    textStyle = AppTheme.types.bodyMedium.copy(color = AppTheme.colors.onSurface),
                    decorationBox = { inner ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        ) {
                            Box(Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Поиск...",
                                        style = AppTheme.types.bodyMedium,
                                        color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                                inner()
                            }
                            if (searchQuery.isNotEmpty() && items.isEmpty()) {
                                AppIcon(
                                    icon = com.vrsalex.uikit.R.drawable.add,
                                    onClick = { onAddClick(searchQuery) },
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items, key = { it.id }) { item ->
                        val isSelected = item.id in selectedIds
                        AppChip(
                            text = item.name,
                            color = item.color,
                            filled = isSelected,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}