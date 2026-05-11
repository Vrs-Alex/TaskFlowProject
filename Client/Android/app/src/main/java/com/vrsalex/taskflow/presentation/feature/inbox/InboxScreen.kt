package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.domain.utils.toComposeColor
import com.vrsalex.taskflow.domain.workscape.area.Area
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.uikit.component.card.EventCard
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.controller.tab.AppSegmentedTabs
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

private val PRESET_COLORS = listOf(
    "#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0", "#42A5F5",
    "#26C6DA", "#26A69A", "#66BB6A", "#D4E157", "#FFEE58", "#FFA726",
    "#FF7043", "#A1887F", "#78909C", "#BDBDBD"
)

@Composable
fun ArchiveScreen(viewModel: InboxViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArchiveContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun ArchiveContent(
    state: InboxContract.State,
    onAction: (InboxContract.Action) -> Unit
) {
    val tabs = InboxContract.Tab.entries.map { it.name to stringResource(it.title) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 144.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(contentType = "Header") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 12.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(com.vrsalex.taskflow.R.string.tab_archive),
                    style = AppTheme.types.headline,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                SearchInput(
                    query = state.searchQuery,
                    onQueryChanged = { onAction(InboxContract.Action.SearchQueryChanged(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                AppSegmentedTabs(
                    items = tabs,
                    selectedId = state.selectedTab.name,
                    onSelect = { id ->
                        onAction(InboxContract.Action.TabSelected(InboxContract.Tab.valueOf(id)))
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        when (state.selectedTab) {
            InboxContract.Tab.ITEMS -> {
                if (state.events.isEmpty()) {
                    item(contentType = "Empty") { EmptyState() }
                }
                items(state.events, key = { it.event.id }, contentType = { "Event" }) { eventUi ->
                    EventCard(
                        title = eventUi.event.base.name,
                        time = eventUi.dateString,
                        areaName = eventUi.event.base.area?.name,
                        areaColor = eventUi.areaColor,
                        tags = eventUi.tags,
                        synced = eventUi.event.isSynced,
                        onClick = { onAction(InboxContract.Action.EventClicked(eventUi)) },
                        modifier = Modifier.padding(horizontal = 12.dp).animateItem()
                    )
                }
            }

            InboxContract.Tab.TAGS -> {
                if (state.tags.isEmpty()) {
                    item(contentType = "Empty") { EmptyState() }
                }
                items(state.tags, key = { it.id }, contentType = { "Tag" }) { tag ->
                    TagItem(
                        tag = tag,
                        onColorClick = {
                            onAction(InboxContract.Action.ColorEditStarted(tag.id, tag.color, isTag = true))
                        },
                        onDelete = { onAction(InboxContract.Action.TagDeleted(tag.id)) },
                        modifier = Modifier.padding(horizontal = 12.dp).animateItem()
                    )
                }
            }

            InboxContract.Tab.AREAS -> {
                if (state.areas.isEmpty()) {
                    item(contentType = "Empty") { EmptyState() }
                }
                items(state.areas, key = { it.id }, contentType = { "Area" }) { area ->
                    AreaItem(
                        area = area,
                        onColorClick = {
                            onAction(InboxContract.Action.ColorEditStarted(area.id, area.color, isTag = false))
                        },
                        onDelete = { onAction(InboxContract.Action.AreaDeleted(area.id)) },
                        modifier = Modifier.padding(horizontal = 12.dp).animateItem()
                    )
                }
            }
        }
    }

    state.colorEdit?.let { edit ->
        ColorPickerDialog(
            initialHex = edit.currentHex,
            onDismiss = { onAction(InboxContract.Action.ColorEditDismissed) },
            onSave = { hex ->
                if (edit.isTag) onAction(InboxContract.Action.TagColorSaved(edit.itemId, hex))
                else onAction(InboxContract.Action.AreaColorSaved(edit.itemId, hex))
            }
        )
    }
}

@Composable
private fun TagItem(
    tag: Tag,
    onColorClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = tag.color.toComposeColor() ?: Color.Gray
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surfaceElevated, RoundedCornerShape(14.dp))
            .padding(start = 12.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColorDot(color = color, onClick = onColorClick)

        AppChip(text = "# ${tag.name}", color = color, filled = false)

        Spacer(modifier = Modifier.weight(1f))

        DeleteButton(onClick = onDelete)
    }
}

@Composable
private fun AreaItem(
    area: Area,
    onColorClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = area.color.toComposeColor() ?: Color.Gray
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surfaceElevated, RoundedCornerShape(14.dp))
            .padding(start = 12.dp, end = 8.dp, top = 14.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColorDot(color = color, onClick = onColorClick)

        Text(
            text = area.name,
            style = AppTheme.types.bodyMedium,
            color = AppTheme.colors.onSurface,
            modifier = Modifier.weight(1f)
        )

        DeleteButton(onClick = onDelete)
    }
}

@Composable
private fun ColorDot(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color, CircleShape)
            .border(1.dp, AppTheme.colors.outline, CircleShape)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(AppTheme.colors.error.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .size(36.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        AppIcon(
            icon = com.vrsalex.uikit.R.drawable.trash,
            tint = AppTheme.colors.error,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun ColorPickerDialog(
    initialHex: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var hexInput by remember { mutableStateOf(initialHex.trimStart('#')) }
    val resolvedColor = "#$hexInput".toComposeColor()
    val isValid = resolvedColor != null && hexInput.length == 6

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surfaceElevated,
        title = {
            Text(
                text = "Цвет",
                style = AppTheme.types.title,
                color = AppTheme.colors.onSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Preset swatches — 2 rows of 8
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PRESET_COLORS.chunked(8).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { hex ->
                                val c = hex.toComposeColor() ?: Color.Gray
                                val isSelected = hexInput.equals(hex.trimStart('#'), ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(c, CircleShape)
                                        .then(
                                            if (isSelected) Modifier.border(2.dp, AppTheme.colors.onSurface, CircleShape)
                                            else Modifier.border(1.dp, AppTheme.colors.outline, CircleShape)
                                        )
                                        .clickable { hexInput = hex.trimStart('#') }
                                )
                            }
                        }
                    }
                }

                // Hex input
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.surface, RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            if (isValid || hexInput.isEmpty()) AppTheme.colors.outline else AppTheme.colors.error,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "#",
                        style = AppTheme.types.body,
                        color = AppTheme.colors.onSurfaceVariant
                    )
                    BasicTextField(
                        value = hexInput,
                        onValueChange = { new ->
                            if (new.length <= 6 && new.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) {
                                hexInput = new
                            }
                        },
                        textStyle = AppTheme.types.body.copy(color = AppTheme.colors.onSurface),
                        cursorBrush = SolidColor(AppTheme.colors.primary),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    if (isValid) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(resolvedColor!!, CircleShape)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (isValid) onSave("#$hexInput") },
                enabled = isValid
            ) {
                Text(
                    text = "Сохранить",
                    style = AppTheme.types.label,
                    color = if (isValid) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Отмена",
                    style = AppTheme.types.label,
                    color = AppTheme.colors.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Пусто",
            style = AppTheme.types.body,
            color = AppTheme.colors.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchInput(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surfaceElevated, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        SmallTextInput(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = stringResource(com.vrsalex.taskflow.R.string.search)
        )
    }
}
