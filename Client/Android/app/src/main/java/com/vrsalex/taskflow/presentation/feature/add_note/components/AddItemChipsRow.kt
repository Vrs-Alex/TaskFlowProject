package com.vrsalex.taskflow.presentation.feature.add_note.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.presentation.common.extension.getItemTypeColor
import com.vrsalex.taskflow.presentation.feature.add_note.AddNoteBaseContract
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.add_note.event.AddItemEventFields
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskContract
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskFields
import com.vrsalex.taskflow.presentation.model.AreaUiModel
import com.vrsalex.taskflow.presentation.model.TagUiModel
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.chip.AppChipMenu
import com.vrsalex.uikit.theme.AppTheme

// Типы, доступные для создания в этом шите (GOAL/HABIT появятся позже)
private val CREATABLE_TYPES = listOf(NoteType.NOTE, NoteType.TASK, NoteType.EVENT)

@Composable
fun AddItemChipsRow(
    type: NoteType,
    selectedArea: AreaUiModel?,
    selectedTags: List<TagUiModel>,
    eventState: AddItemEventContract.State,
    taskState: AddItemTaskContract.State,
    onBaseAction: (AddNoteBaseContract.Action) -> Unit,
    onEventAction: (AddItemEventContract.Action) -> Unit,
    onTaskAction: (AddItemTaskContract.Action) -> Unit,
    onResumeSheet: () -> Unit,
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(contentType = "Type") {
            AppChipMenu(
                selected = type,
                items = CREATABLE_TYPES,
                itemText = { it.name },
                itemColor = { getItemTypeColor(it) },
                onItemSelected = { onBaseAction(AddNoteBaseContract.Action.TypeChanged(it)) }
            )
        }

        item(contentType = "SubFields") {
            AnimatedContent(
                targetState = type,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "type_fields"
            ) { currentType ->
                when (currentType) {
                    NoteType.EVENT -> AddItemEventFields(
                        state = eventState,
                        onAction = onEventAction,
                        onResumeSheet = onResumeSheet
                    )
                    NoteType.TASK -> AddItemTaskFields(
                        state = taskState,
                        onAction = onTaskAction,
                        onResumeSheet = onResumeSheet
                    )
                    else -> return@AnimatedContent
                }
            }
        }

        item(contentType = "Area") {
            AppChip(
                text = selectedArea?.name ?: stringResource(R.string.area),
                color = AppTheme.colors.onSurfaceVariant,
                filled = selectedArea != null,
                onClick = { onBaseAction(AddNoteBaseContract.Action.ShowSelector(SelectorType.AREA)) },
                modifier = Modifier.animateItem()
            )
        }

        item(contentType = "Tags") {
            AppChip(
                text = if (selectedTags.isEmpty()) stringResource(R.string.tag)
                else selectedTags.joinToString(separator = ", ") { it.name },
                color = AppTheme.colors.onSurfaceVariant,
                filled = selectedTags.isNotEmpty(),
                onClick = { onBaseAction(AddNoteBaseContract.Action.ShowSelector(SelectorType.TAGS)) },
                modifier = Modifier.animateItem()
            )
        }
    }
}
