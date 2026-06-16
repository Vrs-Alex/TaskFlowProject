package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.presentation.common.card.EventCard
import com.vrsalex.taskflow.presentation.feature.calendar.components.CalendarHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = koinViewModel<CalendarViewModel>()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    CalendarContent(state, viewModel::onAction, modifier)
}

@Composable
private fun CalendarContent(
    state: CalendarContract.State,
    onAction: (CalendarContract.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarHeader(
            state = state,
            onAction = onAction
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.eventList,
                key = { event -> event.date + event.events }
            ) { uiModel ->
                EventCard(
                    ui = uiModel
                )
            }
        }
    }
}