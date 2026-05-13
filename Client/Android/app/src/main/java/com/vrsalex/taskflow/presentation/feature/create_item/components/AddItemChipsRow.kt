package com.vrsalex.taskflow.presentation.feature.create_item.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.item.base.ItemType
import com.vrsalex.taskflow.presentation.common.extension.getItemTypeColor
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemBaseContract
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventContract
import com.vrsalex.taskflow.presentation.feature.create_item.event.AddItemEventFields
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskContract
import com.vrsalex.taskflow.presentation.feature.create_item.task.AddItemTaskFields
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.taskflow.presentation.feature.workspace.tag.TagUiModel
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.chip.AppChipMenu
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AddItemChipsRow(
    type: ItemType,
    selectedArea: AreaUiModel?,
    selectedTags: List<TagUiModel>,
    eventState: AddItemEventContract.State,
    taskState: AddItemTaskContract.State,
    onBaseAction: (AddItemBaseContract.Action) -> Unit,
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
                items = ItemType.entries,
                itemText = { it.name },
                itemColor = { getItemTypeColor(it) },
                onItemSelected = { onBaseAction(AddItemBaseContract.Action.TypeChanged(it)) }
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
                    ItemType.EVENT -> AddItemEventFields(
                        state = eventState,
                        onAction = onEventAction,
                        onResumeSheet = onResumeSheet
                    )
                    ItemType.TASK -> AddItemTaskFields(
                        state = taskState,
                        onAction = onTaskAction,
                        onResumeSheet = onResumeSheet
                    )
                    ItemType.NOTE -> return@AnimatedContent
                }
            }
        }

        item(contentType = "Area") {
            AppChip(
                text = selectedArea?.name ?: stringResource(R.string.area),
                color = AppTheme.colors.onSurfaceVariant,
                filled = selectedArea != null,
                onClick = { onBaseAction(AddItemBaseContract.Action.ShowSelector(SelectorType.AREA)) },
                modifier = Modifier.animateItem()
            )
        }

        item(contentType = "Tags") {
            AppChip(
                text = if (selectedTags.isEmpty()) stringResource(R.string.tag)
                else selectedTags.joinToString(separator = ", ") { it.name },
                color = AppTheme.colors.onSurfaceVariant,
                filled = selectedTags.isNotEmpty(),
                onClick = { onBaseAction(AddItemBaseContract.Action.ShowSelector(SelectorType.TAGS)) },
                modifier = Modifier.animateItem()
            )
        }
    }
}
