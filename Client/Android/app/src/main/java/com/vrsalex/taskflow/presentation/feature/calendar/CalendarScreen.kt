package com.vrsalex.taskflow.presentation.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.taskflow.presentation.common.card.EventCard
import com.vrsalex.taskflow.presentation.common.extension.monthNameRes
import com.vrsalex.taskflow.presentation.common.extension.weekdayShortRes
import com.vrsalex.taskflow.presentation.feature.calendar.components.CalendarHeader
import com.vrsalex.taskflow.presentation.feature.calendar.components.CalendarHeaderDefaults
import com.vrsalex.taskflow.presentation.feature.calendar.model.CalendarDayState
import com.vrsalex.uikit.theme.AppTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock

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
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Высота свёрнутой шапки приходит замером из CalendarHeader (фолбэк — до первого замера).
    // Шапка плавает поверх списка (haze-блюр), поэтому дни уходят ПОД неё; этот отступ
    // опускает агенду под свёрнутую шапку и используется во всех scroll-to.
    var headerHeight by remember { mutableStateOf(CalendarHeaderDefaults.CollapsedHeightFallback) }
    val headerInset = headerHeight + 8.dp
    val headerInsetPx = with(density) { headerInset.roundToPx() }

    val initialIndex = remember {
        state.days.indexOfFirst { it.date == state.currentDate }.coerceAtLeast(0)
    }

    val bodyState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val headerRowState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    // Стартуем на «сегодня», но опускаем его под шапку (а не под верх вьюпорта).
    LaunchedEffect(Unit) {
        bodyState.scrollToItem(initialIndex, -headerInsetPx)
    }

    // currentDate = первый день, выглядывающий ИЗ-ПОД шапки (а не первый за её блюром).
    val currentIndex by remember(headerInsetPx) {
        derivedStateOf {
            bodyState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.offset + it.size > headerInsetPx }
                ?.index
                ?: bodyState.firstVisibleItemIndex
        }
    }
    val currentDate = state.days.getOrNull(currentIndex)?.date ?: state.currentDate

    LaunchedEffect(currentIndex) {
        if (bodyState.isScrollInProgress) {
            headerRowState.animateScrollToItem(currentIndex)
        }
    }

    val spineStart = state.days.firstOrNull()?.date
    LaunchedEffect(spineStart) {
        if (spineStart == null) return@LaunchedEffect
        snapshotFlow { bodyState.firstVisibleItemIndex }
            .map { spineStart.plus(DatePeriod(days = it)) }
            .map { LocalDate(it.year, it.month, 1) }
            .distinctUntilChanged()
            .collect { onAction(CalendarContract.Action.UpdateVisibleDate(it)) }
    }

    val onDateClick: (LocalDate) -> Unit = { date ->
        val index = state.days.indexOfFirst { it.date == date }
        if (index >= 0) scope.launch { bodyState.animateScrollToItem(index, -headerInsetPx) }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val hazeState = rememberHazeState()

        LazyColumn(
            state = bodyState,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
            contentPadding = PaddingValues(
                top = headerInset,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.days,
                key = { day -> day.date.toString() }
            ) { day ->
                DaySection(day = day)
            }
        }
        CalendarHeader(
            state = state,
            currentDate = currentDate,
            headerRowState = headerRowState,
            hazeState = hazeState,
            onDateClick = onDateClick,
            onAction = onAction,
            onCollapsedHeight = { headerHeight = it },
            onTodayClick = {
                scope.launch {
                    bodyState.animateScrollToItem(initialIndex, -headerInsetPx)
                }
            }
        )
    }
}

@Composable
private fun DaySection(
    day: CalendarDayState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DayHeader(date = day.date, hasAny = day.events.isNotEmpty())
        day.events.forEach { event ->
            EventCard(ui = event)
        }
    }
}

@Composable
private fun DayHeader(
    date: LocalDate,
    hasAny: Boolean,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val isToday = date == today

    val numberColor = when {
        isToday -> AppTheme.colors.primary
        hasAny -> AppTheme.colors.onBackground
        else -> AppTheme.colors.onSurfaceMuted
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = date.day.toString(),
            style = AppTheme.types.title,
            color = numberColor
        )
        Text(
            text = stringResource(date.weekdayShortRes()),
            style = AppTheme.types.label,
            color = AppTheme.colors.onSurfaceVariant
        )
        if (date.day == 1) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(date.month.monthNameRes()),
                style = AppTheme.types.label,
                color = AppTheme.colors.primary
            )
        }
    }
}
