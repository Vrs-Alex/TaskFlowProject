package com.vrsalex.taskflow.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.card.ItemCard
import com.vrsalex.uikit.component.card.ItemCardType
import com.vrsalex.uikit.component.controller.chip.AppFilterChip
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import androidx.core.graphics.toColorInt
import com.vrsalex.taskflow.domain.utils.toComposeColor
import com.vrsalex.taskflow.presentation.common.extension.formatDateRange
import com.vrsalex.uikit.component.card.EventCard
import com.vrsalex.uikit.component.section.AppSectionHeader
import com.vrsalex.uikit.theme.EventHue


@Composable
fun HomeScreen(
    scaffoldPadding: PaddingValues,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(state, viewModel::onAction, scaffoldPadding)
}

@Composable
private fun HomeContent(
    state: HomeContact.State,
    onAction: (HomeContact.Action) -> Unit,
    scaffoldPadding: PaddingValues
) {

    val listState = rememberLazyListState()
    val navPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val floatingPadding = remember { scaffoldPadding.calculateBottomPadding() - navPadding }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = AppTheme.colors.primary,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                AppIcon(
                    icon = R.drawable.pen,
                    tint = AppTheme.colors.onPrimary
                )
            }
        },
        contentWindowInsets = WindowInsets()
    ) { _ ->
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
                        Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(com.vrsalex.taskflow.R.string.today),
                            style = AppTheme.types.bodyMedium,
                            color = AppTheme.colors.onSurfaceVariant,
                        )
                        Text(
                            text = state.todayDate,
                            style = AppTheme.types.headline,
                            color = AppTheme.colors.onSurface,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        Modifier
                            .fillMaxWidth(),
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
                item (contentType = { "EventHeader" }) {
                    AppSectionHeader(
                        title = stringResource(com.vrsalex.taskflow.R.string.event),
                        count = state.eventList.size,
                        accentColor = EventHue,
                        modifier = Modifier.fillMaxWidth()
                            .background(AppTheme.colors.background.copy(alpha = 0.8f))
                            .padding(horizontal = 16.dp)
                            .statusBarsPadding()
                    )
                }

                items(state.eventList, key = { it.id }, contentType = { "Event" }) { event ->
                    val tags = remember(event.base.tags) {
                        event.base.tags.map { Pair(it.name, it.color.toComposeColor()) }
                    }
                    EventCard(
                        title = event.base.name,
                        time = event.formatDateRange(),
                        areaName = event.base.area?.name,
                        areaColor = event.base.area?.color?.toComposeColor(),
                        tags = tags,
                        synced = event.isSynced,
                        onClick = {},
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }

}