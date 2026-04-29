package com.vrsalex.uikit.component.time

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlin.time.Clock

private const val WHEEL_VISIBLE = 5
private val WHEEL_ROW_HEIGHT = 40.dp
private val WHEEL_HEIGHT = WHEEL_ROW_HEIGHT * WHEEL_VISIBLE

private const val HOUR_REPEAT = 100
private const val MIN_REPEAT = 100

private val HOUR_LABELS: List<String> = List(24) { it.toString().padStart(2, '0') }
private val MIN_LABELS: List<String> = List(60) { it.toString().padStart(2, '0') }

data class PickedDateTime(
    val dateTime: LocalDateTime,
    val isAllDay: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateTimePicker(
    initial: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    initialAllDay: Boolean = false,
    title: String = "Выбрать дату",
    isAllDay: Boolean = false,
    onConfirm: (PickedDateTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tz = TimeZone.currentSystemDefault()
    val today = remember { Clock.System.now().toLocalDateTime(tz).date }

    val dates = remember {
        (-7..365).map { today.plus(it, DateTimeUnit.DAY) }
    }
    val initialDateIndex = remember(initial) {
        dates.indexOfFirst { it == initial.date }.coerceAtLeast(0)
    }

    val todayLbl = stringResource(R.string.today)
    val tomorrowLbl = stringResource(R.string.tomorrow)
    val yesterdayLbl = stringResource(R.string.yesterday)
    val dateLabels = remember(dates, todayLbl, tomorrowLbl, yesterdayLbl) {
        dates.map { formatDateLabelPure(it, today, todayLbl, tomorrowLbl, yesterdayLbl) }
    }

    val initialHourIdx = remember { 24 * (HOUR_REPEAT / 2) + initial.hour }
    val initialMinIdx = remember { 60 * (MIN_REPEAT / 2) + initial.minute }

    var selectedDateIndex by rememberSaveable { mutableIntStateOf(initialDateIndex) }
    var selectedHour by rememberSaveable { mutableIntStateOf(initial.hour) }
    var selectedMinute by rememberSaveable { mutableIntStateOf(initial.minute) }
    var isAllDay by rememberSaveable { mutableStateOf(initialAllDay) }

    val current by remember {
        derivedStateOf {
            val date = dates[selectedDateIndex]
            val time = if (isAllDay) LocalTime(0, 0) else LocalTime(selectedHour, selectedMinute)
            LocalDateTime(date, time)
        }
    }

    val allDayLbl = stringResource(R.string.all_day)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.colors.surfaceElevated,
        scrimColor = AppTheme.colors.scrim,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp, top = 20.dp),
        ) {
            Text(
                title,
                style = AppTheme.types.titleLarge,
                color = AppTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(WHEEL_HEIGHT),
            ) {
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(WHEEL_ROW_HEIGHT)
                        .background(AppTheme.colors.surface, RoundedCornerShape(10.dp))
                        .border(1.dp, AppTheme.colors.outline, RoundedCornerShape(10.dp)),
                )

                Row(Modifier.fillMaxSize()) {
                    WheelColumn(
                        modifier = Modifier.weight(2f),
                        items = dateLabels,
                        itemCount = dateLabels.size,
                        initialIndex = initialDateIndex,
                        onSelect = { selectedDateIndex = it },
                    )

                    AnimatedVisibility(
                        visible = !isAllDay,
                        enter = fadeIn(tween(200)),
                        exit = fadeOut(tween(150)),
                        modifier = Modifier.weight(if (isAllDay) 0.0001f else 2.1f),
                    ) {
                        Row(Modifier.fillMaxSize()) {
                            WheelColumn(
                                modifier = Modifier.weight(1f),
                                itemAt = { i -> HOUR_LABELS[i % 24] },
                                itemCount = 24 * HOUR_REPEAT,
                                initialIndex = initialHourIdx,
                                onSelect = { selectedHour = it % 24 },
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.width(10.dp).fillMaxHeight(),
                            ) {
                                Text(
                                    ":",
                                    style = AppTheme.types.titleLarge,
                                    color = AppTheme.colors.onSurface,
                                )
                            }
                            WheelColumn(
                                modifier = Modifier.weight(1f),
                                itemAt = { i -> MIN_LABELS[i % 60] },
                                itemCount = 60 * MIN_REPEAT,
                                initialIndex = initialMinIdx,
                                onSelect = { selectedMinute = it % 60 },
                            )
                        }
                    }
                }

                // Градиенты сверху и снизу
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(WHEEL_ROW_HEIGHT * 2)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    AppTheme.colors.surfaceElevated,
                                    AppTheme.colors.surfaceElevated.copy(alpha = 0f),
                                ),
                            ),
                        ),
                )
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(WHEEL_ROW_HEIGHT * 2)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    AppTheme.colors.surfaceElevated.copy(alpha = 0f),
                                    AppTheme.colors.surfaceElevated,
                                ),
                            ),
                        ),
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = AppTheme.shapes.medium,
                    border = BorderStroke(1.dp, AppTheme.colors.outline),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AppTheme.colors.onSurface,
                    ),
                ) {
                    Text(stringResource(R.string.cancel), style = AppTheme.types.button)
                }
                Button(
                    onClick = { onConfirm(PickedDateTime(current, isAllDay)) },
                    modifier = Modifier.weight(1.5f),
                    shape = AppTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.primary,
                        contentColor = AppTheme.colors.onPrimary,
                    ),
                ) {
                    Text(
                        formatPreviewShort(current, today, isAllDay, todayLbl, tomorrowLbl),
                        style = AppTheme.types.button,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun WheelColumn(
    initialIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<String>? = null,
    itemAt: ((Int) -> String)? = null,
    itemCount: Int = items?.size ?: 0,
) {
    require(items != null || itemAt != null) { "Either items or itemAt must be provided" }
    val getItem: (Int) -> String = items?.let { { i -> it[i] } } ?: itemAt!!

    val state: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex,
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    val scope = rememberCoroutineScope()

    val centeredIndex by remember(itemCount) {
        derivedStateOf {
            val first = state.firstVisibleItemIndex
            val offset = state.firstVisibleItemScrollOffset
            val rowPx = state.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
            val extra = if (rowPx > 0) (offset + rowPx / 2) / rowPx else 0
            (first + extra).coerceIn(0, itemCount - 1)
        }
    }

    LaunchedEffect(state, itemCount) {
        snapshotFlow { centeredIndex }
            .distinctUntilChanged()
            .collect { idx -> onSelect(idx.coerceIn(0, itemCount - 1)) }
    }

    LazyColumn(
        state = state,
        flingBehavior = flingBehavior,
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(vertical = WHEEL_ROW_HEIGHT * 2),
    ) {
        items(
            count = itemCount,
            key = { it },
        ) { i ->
            val center = centeredIndex
            val distance = if (i >= center) i - center else center - i
            val isCenter = distance == 0
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WHEEL_ROW_HEIGHT)
                    .clickable { scope.launch { state.animateScrollToItem(i) } }
                    .graphicsLayer {
                        alpha = when (distance) {
                            0 -> 1f
                            1 -> 0.55f
                            2 -> 0.28f
                            else -> 0.15f
                        }
                    }
            ) {
                Text(
                    text = getItem(i),
                    style = if (isCenter) AppTheme.types.titleLarge else AppTheme.types.title,
                    color = if (isCenter) AppTheme.colors.onSurface
                    else AppTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

private fun formatDateLabelPure(
    date: LocalDate,
    today: LocalDate,
    todayLbl: String,
    tomorrowLbl: String,
    yesterdayLbl: String,
): String = when (date) {
    today -> todayLbl
    today.plus(1, DateTimeUnit.DAY) -> tomorrowLbl
    today.minus(1, DateTimeUnit.DAY) -> yesterdayLbl
    else -> {
        val javaDate = java.time.LocalDate.of(date.year, date.month.number, date.day)
        val locale = java.util.Locale.getDefault()
        val weekday = javaDate.dayOfWeek
            .getDisplayName(java.time.format.TextStyle.SHORT, locale)
            .replaceFirstChar { it.titlecase(locale) }
        val month = javaDate.month
            .getDisplayName(java.time.format.TextStyle.SHORT, locale)
            .trimEnd('.')
        "$weekday, ${date.dayOfMonth} $month"
    }
}


private fun formatPreviewShort(
    dt: LocalDateTime,
    today: LocalDate,
    isAllDay: Boolean,
    todayLbl: String,
    tomorrowLbl: String,
): String {
    val datePart = when (dt.date) {
        today -> todayLbl
        today.plus(1, DateTimeUnit.DAY) -> tomorrowLbl
        else -> {
            val javaDate = java.time.LocalDate.of(dt.year, dt.month.number, dt.day)
            javaDate.format(java.time.format.DateTimeFormatter.ofPattern("d MMM"))
        }
    }
    return if (isAllDay) datePart else {
        val h = dt.hour.toString().padStart(2, '0')
        val m = dt.minute.toString().padStart(2, '0')
        "$datePart, $h:$m"
    }
}