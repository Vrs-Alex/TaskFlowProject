package com.vrsalex.taskflow.presentation.feature.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.card.ItemCard
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.controller.chip.AppConnectedIndicator
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.component.menu.SortMenu
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun InboxScreen(
    viewModel: InboxViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InboxContent(state = state, viewModel::onAction)
}

@Composable
private fun InboxContent(
    state: InboxContract.State,
    action: (InboxContract.Action) -> Unit
) {

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.tab_inbox),
                        style = AppTheme.types.headline,
                        color = AppTheme.colors.onSurface,
                    )
                    AppConnectedIndicator(connect = state.isConnected)

                    Spacer(Modifier.weight(1f))
                    SortMenu(
                        options = InboxContract.FilterListBy.entries,
                        selected = state.filtersBy,
                        label = { stringResource(it.title) },
                        onSelect = { action(InboxContract.Action.OnChangeFilter(it)) }
                    ) {
                        AppIcon(
                            icon = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.filter),
                            tint = AppTheme.colors.onBackground
                        )
                    }

                }
                SearchInput(
                    query = state.searchQuery,
                    onQueryChanged = { },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        items(state.overdueTasks, key = { it.task.id }){ task ->
            ItemCard(
                type = ItemCardType.Task,
                title = task.task.base.name,
                subline = {
                    Row() {
                        Text(
                            text = "Просрочено " + task.dueDateString,
                            color = AppTheme.colors.error
                        )

                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp).animateItem(),
                areaName = task.task.base.area?.name,
                areaColor = task.areaColor,
                tags = task.tags,
                synced = task.task.base.isSynced,
                onClick = {}
            )
        }

        items(state.notes, key = { it.note.id }){ note ->
            ItemCard(
                type = null,
                title = note.note.name,
                subline = {},
                modifier = Modifier.padding(horizontal = 20.dp).animateItem(),
                areaName = note.note.area?.name,
                areaColor = note.areaColor,
                tags = note.tags,
                synced = note.note.isSynced,
                onClick = {}
            )
        }
    }
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
            placeholder = stringResource(R.string.search)
        )
    }
}
