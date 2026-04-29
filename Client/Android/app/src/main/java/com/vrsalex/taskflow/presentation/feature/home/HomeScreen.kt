package com.vrsalex.taskflow.presentation.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.presentation.feature.create_item.AddItemContent
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.chip.AppFilterChip
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import com.vrsalex.uikit.component.card.EventCard
import com.vrsalex.uikit.component.controller.chip.AppSyncIndicator
import com.vrsalex.uikit.component.section.AppSectionHeader
import com.vrsalex.uikit.theme.EventHue


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(state, viewModel::onAction)

}

@Composable
private fun HomeContent(
    state: HomeContact.State,
    onAction: (HomeContact.Action) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 144.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        item(contentType = "Title") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(com.vrsalex.taskflow.R.string.today),
                            style = AppTheme.types.bodyMedium,
                            color = AppTheme.colors.onSurfaceVariant,
                        )
                        AppSyncIndicator(isSynced = state.isConnected)
                    }
                    AnimatedContent(state.todayDate) { string ->
                        Text(
                            text = string,
                            style = AppTheme.types.headline,
                            color = AppTheme.colors.onSurface,
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    items(HomeContact.FilterChip.entries) { filter ->
                        AppFilterChip(
                            text = stringResource(filter.title),
                            selected = state.selectedFilterChip == filter,
                            onClick = { onAction(HomeContact.Action.FilterChipSelected(filter)) }
                        )
                    }
                }
            }
        }


        if (state.selectedFilterChip == HomeContact.FilterChip.ALL || state.selectedFilterChip == HomeContact.FilterChip.EVENT) {
            item(contentType = { "EventHeader" }) {
                AppSectionHeader(
                    title = stringResource(com.vrsalex.taskflow.R.string.events),
                    count = state.eventList.size,
                    accentColor = EventHue,
                    modifier = Modifier.fillMaxWidth()
                        .background(AppTheme.colors.background.copy(alpha = 0.8f))
                        .padding(horizontal = 16.dp).padding(top = 8.dp)
                )
            }

            items(state.eventList, key = { it.event.id }, contentType = { "Event" }) { eventUi ->
                EventCard(
                    title = eventUi.event.base.name,
                    time = eventUi.dateString,
                    areaName = eventUi.event.base.area?.name,
                    areaColor = eventUi.areaColor,
                    tags = eventUi.tags,
                    synced = eventUi.event.isSynced,
                    onClick = { onAction(HomeContact.Action.EventClicked(eventUi)) },
                    modifier = Modifier.padding(horizontal = 12.dp).animateItem()
                )
            }
        }
    }
}