package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.component.card.TaskCard
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import org.koin.androidx.compose.koinViewModel
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Build (listIndex → date) mapping for ALL visible dates (including empty ones)
    val indexToDate = remember(state.visibleDates, state.tasksByDate) {
        buildList {
            var index = 0
            state.visibleDates.forEach { date ->
                add(index to date)
                index += 1 + (state.tasksByDate[date]?.size ?: 0)
            }
        }
    }
    val currentIndexToDate by rememberUpdatedState(indexToDate)

    // Flag to suppress scroll-detection updates during programmatic scroll
    val isProgrammaticScroll = remember { mutableStateOf(false) }

    // Scroll list when user taps a date in the calendar
    LaunchedEffect(Unit) {
        viewModel.scrollToDate.collect { date ->
            val idx = currentIndexToDate.firstOrNull { (_, d) -> d == date }?.first ?: return@collect
            isProgrammaticScroll.value = true
            listState.animateScrollToItem(idx)
            isProgrammaticScroll.value = false
        }
    }

    // Update selected day in calendar when user scrolls the list
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .drop(1) // skip initial emission
            .collect { firstIndex ->
                if (isProgrammaticScroll.value) return@collect
                val date = currentIndexToDate
                    .lastOrNull { (idx, _) -> idx <= firstIndex }
                    ?.second ?: return@collect
                viewModel.onAction(CalendarContract.Action.DateScrolled(date))
            }
    }

    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        CalendarWidget(state = state, onAction = viewModel::onAction)

        HorizontalDivider(color = AppTheme.colors.surfaceVariant)

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 144.dp, top = 4.dp),
        ) {
            state.visibleDates.forEach { date ->
                val tasks = state.tasksByDate[date] ?: emptyList()
                stickyHeader(key = "header_$date") {
                    CalendarDateHeader(date = date, count = tasks.size)
                }
                items(
                    items = tasks,
                    key = { "task_${it.task.base.id}" },
                    contentType = { "Task" },
                ) { task ->
                    TaskCard(
                        title = task.task.base.name,
                        dueDate = null,
                        time = task.task.dueTime?.let {
                            "${it.hour.toString().padStart(2, '0')}:${it.minute.toString().padStart(2, '0')}"
                        },
                        isCompleted = task.isCompleted,
                        areaColor = task.areaColor,
                        tags = task.tags,
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        onCheckedChange = {
                            viewModel.onAction(
                                CalendarContract.Action.TaskCheckBoxToggled(task, date)
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarWidget(
    state: CalendarContract.State,
    onAction: (CalendarContract.Action) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surface)
            .animateContentSize()
            .padding(bottom = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = state.isExpanded) {
                IconButton(onClick = { onAction(CalendarContract.Action.PreviousMonth) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.tab_calendar),
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface,
                    )
                }
            }

            Text(
                text = state.displayMonth.formatMonthYear(),
                style = AppTheme.types.title,
                color = AppTheme.colors.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )

            AnimatedVisibility(visible = state.isExpanded) {
                IconButton(onClick = { onAction(CalendarContract.Action.NextMonth) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.tab_calendar),
                        contentDescription = null,
                        tint = AppTheme.colors.onSurface,
                    )
                }
            }

            IconButton(onClick = { onAction(CalendarContract.Action.ToggleExpanded) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.vrsalex.uikit.R.drawable.tab_calendar),
                    contentDescription = null,
                    tint = AppTheme.colors.onSurface,
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            for (dayNum in 1..7) {
                Text(
                    text = java.time.DayOfWeek.of(dayNum)
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        .take(2)
                        .uppercase(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = AppTheme.types.micro,
                    color = AppTheme.colors.onSurfaceMuted,
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        AnimatedContent(
            targetState = state.isExpanded,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
            label = "calendar_mode",
        ) { expanded ->
            if (expanded) {
                CalendarMonthGrid(state = state, onAction = onAction)
            } else {
                CalendarWeekRow(state = state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun CalendarWeekRow(
    state: CalendarContract.State,
    onAction: (CalendarContract.Action) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        state.visibleDates.forEach { date ->
            CalendarDayCell(
                date = date,
                isSelected = date == state.selectedDate,
                isToday = date == state.today,
                hasTask = state.tasksByDate.containsKey(date),
                modifier = Modifier.weight(1f),
                onClick = { onAction(CalendarContract.Action.DateSelected(date)) },
            )
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    state: CalendarContract.State,
    onAction: (CalendarContract.Action) -> Unit,
) {
    val startOffset = state.displayMonth.dayOfWeek.isoDayNumber - 1
    val daysInMonth = state.visibleDates.size
    val rows = (startOffset + daysInMonth + 6) / 7

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val dayIndex = row * 7 + col - startOffset
                    if (dayIndex < 0 || dayIndex >= daysInMonth) {
                        Spacer(Modifier.weight(1f))
                    } else {
                        val date = state.visibleDates[dayIndex]
                        CalendarDayCell(
                            date = date,
                            isSelected = date == state.selectedDate,
                            isToday = date == state.today,
                            hasTask = state.tasksByDate.containsKey(date),
                            modifier = Modifier.weight(1f),
                            onClick = { onAction(CalendarContract.Action.DateSelected(date)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    hasTask: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isSelected -> AppTheme.colors.primary
                        isToday -> AppTheme.colors.primarySoft
                        else -> Color.Transparent
                    }
                ),
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = AppTheme.types.bodyMedium,
                color = when {
                    isSelected -> AppTheme.colors.onPrimary
                    isToday -> AppTheme.colors.primary
                    else -> AppTheme.colors.onSurface
                },
                textAlign = TextAlign.Center,
            )
        }

        Box(
            Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    when {
                        !hasTask -> Color.Transparent
                        isSelected -> AppTheme.colors.onPrimary.copy(alpha = 0.7f)
                        else -> AppTheme.colors.primary
                    }
                )
        )
    }
}

@Composable
private fun CalendarDateHeader(date: LocalDate, count: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date.formatSectionDate().uppercase(),
            style = AppTheme.types.caption,
            color = AppTheme.colors.onSurfaceVariant,
        )
        Text(
            text = count.toString(),
            style = AppTheme.types.micro,
            color = AppTheme.colors.onSurfaceMuted,
        )
    }
}

private fun LocalDate.formatMonthYear(): String {
    val monthName = java.time.Month.of(monthNumber)
        .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        .replaceFirstChar { it.uppercase() }
    return "$monthName $year"
}

private fun LocalDate.formatSectionDate(): String {
    val jDate = java.time.LocalDate.of(year, monthNumber, dayOfMonth)
    val dayName = jDate.dayOfWeek
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.uppercase() }
    val monthName = jDate.month
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.uppercase() }
    return "$dayName, $dayOfMonth $monthName"
}
