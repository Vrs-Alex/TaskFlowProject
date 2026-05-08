package com.vrsalex.taskflow.presentation.feature.create_item

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.feature.create_item.components.AddItemBaseFields
import com.vrsalex.taskflow.presentation.feature.create_item.components.AddItemChipsRow
import com.vrsalex.taskflow.presentation.feature.create_item.components.ItemSelectorOverlay
import com.vrsalex.taskflow.presentation.feature.create_item.components.SelectorType
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventOptFields
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskContract
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AddItemContent(
    viewModel: AddItemViewModel,
    isVisible: Boolean,
    onClose: () -> Unit,
) {
    BackHandler(enabled = isVisible) { onClose() }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.scrim.copy(alpha = 0.7f))
                    .pointerInput(Unit) { detectTapGestures { onClose() } }
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
                AddItemSheetContent(viewModel, onClose)
            }
        }
    }
}

@Composable
private fun AddItemSheetContent(
    viewModel: AddItemViewModel,
    onClose: () -> Unit,
) {
    val baseState by viewModel.state.collectAsStateWithLifecycle()
    val eventState by viewModel.eventVm.state.collectAsStateWithLifecycle()
    val taskState by viewModel.taskVm.state.collectAsStateWithLifecycle()

    val isActiveSubmit = remember(baseState.title, baseState.type, eventState.isValid, taskState.isValid) {
        baseState.title.isNotBlank() && when (baseState.type) {
            ItemType.EVENT -> eventState.isValid
            ItemType.TASK -> taskState.isValid
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddItemBaseContract.Effect.ResumeSheet -> keyboardController?.show()
                AddItemBaseContract.Effect.Dismiss -> onClose()
            }
        }
    }

    Column(Modifier.fillMaxWidth()) {
        ItemSelectorOverlay(
            visible = baseState.activeSelector != SelectorType.NONE,
            searchQuery = baseState.selectorSearch,
            onSearchQueryChange = {
                viewModel.onAction(
                    AddItemBaseContract.Action.SelectorSearchChanged(
                        it
                    )
                )
            },
            onDismiss = { viewModel.onAction(AddItemBaseContract.Action.HideSelector) },
            onAddClick = { viewModel.onAction(AddItemBaseContract.Action.SelectorCreate(it)) },
            items = baseState.filteredSelectorItems,
            selectedIds = baseState.selectedSelectorIds,
            onItemClick = { item ->
                when (baseState.activeSelector) {
                    SelectorType.TAGS -> {
                        val tag = baseState.availableTags.first { it.id == item.id }
                        viewModel.onAction(AddItemBaseContract.Action.TagToggled(tag))
                    }

                    SelectorType.AREA -> {
                        val area = baseState.availableAreas.firstOrNull { it.id == item.id }
                        viewModel.onAction(AddItemBaseContract.Action.AreaChanged(area))
                    }

                    SelectorType.NONE -> {}
                }
            }
        )

        Box(
            Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.extraLarge)
                .background(AppTheme.colors.surfaceElevated)
                .pointerInput(Unit) { detectTapGestures { } }
        ) {
            AddItemFormContent(
                baseState = baseState,
                eventState = eventState,
                taskState = taskState,
                isActiveSubmit = isActiveSubmit,
                onBaseAction = viewModel::onAction,
                onEventAction = viewModel.eventVm::onAction,
                onTaskAction = viewModel.taskVm::onAction,
                onSave = viewModel::save,
                onResumeSheet = viewModel::resumeSheet,
            )
        }
    }
}

@Composable
private fun AddItemFormContent(
    baseState: AddItemBaseContract.State,
    eventState: AddItemEventContract.State,
    taskState: AddItemTaskContract.State,
    isActiveSubmit: Boolean,
    onBaseAction: (AddItemBaseContract.Action) -> Unit,
    onEventAction: (AddItemEventContract.Action) -> Unit,
    onTaskAction: (AddItemTaskContract.Action) -> Unit,
    onSave: () -> Unit,
    onResumeSheet: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(baseState.activeSelector == SelectorType.NONE, baseState.type) {
        focusRequester.requestFocus()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 12.dp)
    ) {
        AddItemBaseFields(
            title = baseState.title,
            description = baseState.description,
            isActiveSubmit = isActiveSubmit,
            focusRequester = focusRequester,
            onTitleChanged = { onBaseAction(AddItemBaseContract.Action.TitleChanged(it)) },
            onDescriptionChanged = { onBaseAction(AddItemBaseContract.Action.DescriptionChanged(it)) },
            onSave = onSave,
        )

        Spacer(Modifier.height(16.dp))

        AddItemChipsRow(
            type = baseState.type,
            selectedArea = baseState.selectedArea,
            selectedTags = baseState.selectedTags,
            eventState = eventState,
            taskState = taskState,
            onBaseAction = onBaseAction,
            onEventAction = onEventAction,
            onTaskAction = onTaskAction,
            onResumeSheet = onResumeSheet,
        )

        AnimatedContent(
            targetState = baseState.type,
            transitionSpec = {
                fadeIn(tween(200)) + expandHorizontally(tween(250)) togetherWith
                        fadeOut(tween(150)) + shrinkHorizontally(tween(200))
            },
            label = "opt_fields"
        ) { type ->
            when (type) {
                ItemType.EVENT -> AddItemEventOptFields(
                    state = eventState,
                    onAction = onEventAction
                )
                ItemType.TASK -> {}
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
