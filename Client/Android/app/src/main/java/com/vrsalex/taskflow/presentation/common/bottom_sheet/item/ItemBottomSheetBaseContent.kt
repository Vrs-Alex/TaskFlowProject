package com.vrsalex.taskflow.presentation.common.bottom_sheet.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import com.vrsalex.taskflow.presentation.feature.workspace.area.AreaUiModel
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.card.ItemTypeBadge
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.EventHue
import com.vrsalex.uikit.theme.GoalHue
import com.vrsalex.uikit.theme.HabitHue
import com.vrsalex.uikit.theme.TaskHue

@Composable
fun ItemBottomSheetBaseContent(
    itemType: ItemCardType,
    header: String,
    title: String,
    description: String?,
    area: AreaUiModel?,
    tags: List<Pair<String, Color>>,
    synced: Boolean,
    status: ItemStatus,
    subline: @Composable () -> Unit,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onArchive: (status: ItemStatus) -> Unit,
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String?) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(1f)
            ) {
                ItemTypeBadge(
                    type = itemType,
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = header.uppercase(),
                        style = AppTheme.types.label,
                        color = when (itemType) {
                            ItemCardType.Event -> EventHue
                            ItemCardType.Task -> TaskHue
                            ItemCardType.Goal -> GoalHue
                            ItemCardType.Habit -> HabitHue
                        }
                    )
                    var isTitleFocused by remember { mutableStateOf(false) }
                    var localTitle by remember { mutableStateOf(title) }

                    LaunchedEffect(title) {
                        if (!isTitleFocused) localTitle = title
                    }

                    BasicTextField(
                        value = localTitle,
                        onValueChange = { localTitle = it; onTitleChanged(it) },
                        textStyle = AppTheme.types.headline.copy(color = AppTheme.colors.onSurface),
                        cursorBrush = SolidColor(AppTheme.colors.primary),
                        maxLines = 2,
                        modifier = Modifier.onFocusChanged { isTitleFocused = it.isFocused }
                    )
                }
            }

            IconButton(
                onClick = onClose,
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = null,
                    tint = AppTheme.colors.onSurfaceVariant
                )
            }
        }

        subline()

        if (area != null || tags.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.surfaceElevated,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    area?.let { area ->
                        AppChip(
                            text = area.name,
                            color = area.color,
                            filled = true,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
                if (tags.isNotEmpty()) {
                    items(tags) { (name, color) ->
                        AppChip(
                            text = name,
                            color = color,
                            filled = false,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }

        var isDescriptionFocused by remember { mutableStateOf(false) }
        val focusDescription = remember { FocusRequester() }
        var localDescription by remember { mutableStateOf(description) }

        LaunchedEffect(description, isDescriptionFocused) {
            if (!isDescriptionFocused) localDescription = description
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp, min = 64.dp)
                .background(
                    color = AppTheme.colors.surfaceElevated,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable { focusDescription.requestFocus() }
                .verticalScroll(rememberScrollState())
                .nestedScroll(rememberNestedScrollInteropConnection())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            BasicTextField(
                value = localDescription ?: "",
                onValueChange = {
                    localDescription = it;
                    if (it.isBlank()) {
                        onDescriptionChanged(null)
                    } else onDescriptionChanged(it)
                },
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusDescription)
                    .onFocusChanged { isDescriptionFocused = it.isFocused },
                textStyle = AppTheme.types.body.copy(color = AppTheme.colors.onSurface),
                cursorBrush = SolidColor(AppTheme.colors.primary),
                decorationBox = { innerField ->
                    Box() {
                        if (localDescription == null) {
                            Text(
                                text = stringResource(com.vrsalex.taskflow.R.string.description),
                                style = AppTheme.types.body.copy(color = AppTheme.colors.onSurfaceVariant)
                            )
                        }
                        innerField()
                    }
                }
            )

        }

        // TODO REMINDER, ATTACHMENT

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {

            IconButton(
                onClick = {
                    if (status == ItemStatus.ARCHIVED) {
                        onArchive(ItemStatus.ACTIVE)
                    } else {
                        onArchive(ItemStatus.ARCHIVED)
                    }
                },
                modifier = Modifier
                    .background(
                        AppTheme.colors.warning.copy(alpha = 0.15f),
                        RoundedCornerShape(14.dp)
                    )
                    .size(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                AppIcon(
                    icon = R.drawable.tab_archive,
                    tint = AppTheme.colors.warning
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .background(AppTheme.colors.error.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                    .size(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                AppIcon(
                    icon = R.drawable.trash,
                    tint = AppTheme.colors.error
                )
            }
        }

    }
}