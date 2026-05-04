package com.vrsalex.taskflow.presentation.feature.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.uikit.component.card.EventCard
import com.vrsalex.uikit.component.controller.chip.AppFilterChipRow
import com.vrsalex.uikit.component.input.SmallTextInput
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArchiveScreen(
    viewModel: ArchiveViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArchiveContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun ArchiveContent(
    state: ArchiveContract.State,
    onAction: (ArchiveContract.Action) -> Unit
) {
    val filterItems = ArchiveContract.Filter.entries.map { filter ->
        filter.name to stringResource(filter.title)
    }

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
                    .padding(top = 12.dp),
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
                    onQueryChanged = { onAction(ArchiveContract.Action.SearchQueryChanged(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                AppFilterChipRow(
                    items = filterItems,
                    selectedId = state.selectedFilter.name,
                    onSelect = { id ->
                        val filter = ArchiveContract.Filter.valueOf(id)
                        onAction(ArchiveContract.Action.FilterSelected(filter))
                    }
                )
            }
        }

        if (state.events.isEmpty()) {
            item(contentType = "Empty") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Архив пуст",
                        style = AppTheme.types.body,
                        color = AppTheme.colors.onSurfaceVariant
                    )
                }
            }
        }

        items(state.events, key = { it.event.id }, contentType = { "Event" }) { eventUi ->
            EventCard(
                title = eventUi.event.base.name,
                time = eventUi.dateString,
                areaName = eventUi.event.base.area?.name,
                areaColor = eventUi.areaColor,
                tags = eventUi.tags,
                synced = eventUi.event.isSynced,
                onClick = { onAction(ArchiveContract.Action.EventClicked(eventUi)) },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .animateItem()
            )
        }
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
            .background(
                color = AppTheme.colors.surfaceElevated,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        SmallTextInput(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = stringResource(com.vrsalex.taskflow.R.string.search)
        )
    }
}
