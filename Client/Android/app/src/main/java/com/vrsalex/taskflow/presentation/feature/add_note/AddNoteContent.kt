package com.vrsalex.taskflow.presentation.feature.add_note

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.presentation.feature.add_note.components.AddItemBaseFields
import com.vrsalex.taskflow.presentation.feature.add_note.components.AddItemChipsRow
import com.vrsalex.taskflow.presentation.feature.add_note.components.ItemSelectorOverlay
import com.vrsalex.taskflow.presentation.feature.add_note.components.SelectorType
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventOptFields
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskContract
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AddNoteContent(
    viewModel: AddNoteViewModel,
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
                    .align(Alignment.BottomCenter)
            ) {
                AddNoteSheetContent(viewModel, onClose)
            }
        }
    }
}

@Composable
private fun AddNoteSheetContent(
    viewModel: AddNoteViewModel,
    onClose: () -> Unit,
) {
    val baseState by viewModel.state.collectAsStateWithLifecycle()
    val eventState by viewModel.eventVm.state.collectAsStateWithLifecycle()
    val taskState by viewModel.taskVm.state.collectAsStateWithLifecycle()

    val isActiveSubmit = remember(baseState.title, baseState.type, eventState.isValid, taskState.isValid) {
        baseState.title.isNotBlank() && when (baseState.type) {
            NoteType.EVENT -> eventState.isValid
            NoteType.TASK -> taskState.isValid
            else -> true
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AddNoteBaseContract.Effect.ResumeSheet -> keyboardController?.show()
                AddNoteBaseContract.Effect.Dismiss -> onClose()
            }
        }
    }

    Column(Modifier.fillMaxWidth()) {
        ItemSelectorOverlay(
            visible = baseState.activeSelector != SelectorType.NONE,
            searchQuery = baseState.selectorSearch,
            onSearchQueryChange = {
                viewModel.onAction(
                    AddNoteBaseContract.Action.SelectorSearchChanged(
                        it
                    )
                )
            },
            onDismiss = { viewModel.onAction(AddNoteBaseContract.Action.HideSelector) },
            onAddClick = { viewModel.onAction(AddNoteBaseContract.Action.SelectorCreate(it)) },
            items = baseState.filteredSelectorItems,
            selectedIds = baseState.selectedSelectorIds,
            onItemClick = { item ->
                when (baseState.activeSelector) {
                    SelectorType.TAGS -> {
                        val tag = baseState.availableTags.first { it.id == item.id }
                        viewModel.onAction(AddNoteBaseContract.Action.TagToggled(tag))
                    }

                    SelectorType.AREA -> {
                        val area = baseState.availableAreas.firstOrNull { it.id == item.id }
                        if (area?.id == baseState.selectedArea?.id)
                            viewModel.onAction(AddNoteBaseContract.Action.AreaChanged(null))
                        else
                            viewModel.onAction(AddNoteBaseContract.Action.AreaChanged(area))
                    }

                    SelectorType.NONE -> {}
                }
            }
        )

        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(AppTheme.colors.surfaceElevated)
                .navigationBarsPadding()
                .pointerInput(Unit) { detectTapGestures { } }
        ) {
            AddMoteFormContent(
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
private fun AddMoteFormContent(
    baseState: AddNoteBaseContract.State,
    eventState: AddItemEventContract.State,
    taskState: AddItemTaskContract.State,
    isActiveSubmit: Boolean,
    onBaseAction: (AddNoteBaseContract.Action) -> Unit,
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
            onTitleChanged = { onBaseAction(AddNoteBaseContract.Action.TitleChanged(it)) },
            onDescriptionChanged = { onBaseAction(AddNoteBaseContract.Action.DescriptionChanged(it)) },
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
                NoteType.EVENT -> AddItemEventOptFields(
                    state = eventState,
                    onAction = onEventAction
                )
                else -> {}
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
