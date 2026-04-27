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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.checkbox.AppCheckbox
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

private const val HOUR_REPEAT = 1000
private const val MIN_REPEAT = 1000

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

    val initialHourIdx = remember { 24 * (HOUR_REPEAT / 2) + initial.hour }
    val initialMinIdx = remember { 60 * (MIN_REPEAT / 2) + initial.minute }

    var selectedDateIndex by rememberSaveable { mutableIntStateOf(initialDateIndex) }
    var selectedHour by rememberSaveable { mutableIntStateOf(initial.hour) }
    var selectedMinute by rememberSaveable { mutableIntStateOf(initial.minute) }
    var isAllDay by rememberSaveable { mutableStateOf(initialAllDay) }

    val current = remember(selectedDateIndex, selectedHour, selectedMinute, isAllDay) {
        val date = dates[selectedDateIndex]
        val time = if (isAllDay) LocalTime(0, 0) else LocalTime(selectedHour, selectedMinute)
        LocalDateTime(date, time)
    }

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

            AnimatedContent(
                targetState = formatPreview(current, today, isAllDay),
                transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(100)) },
                label = "preview"
            ) { preview ->
                Text(
                    text = preview,
                    style = AppTheme.types.bodyMedium,
                    color = AppTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }

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
                        items = dates.map { formatDateLabel(it, today) },
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
                                items = List(24 * HOUR_REPEAT) { (it % 24).toString().padStart(2, '0') },
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
                                items = List(60 * MIN_REPEAT) { (it % 60).toString().padStart(2, '0') },
                                initialIndex = initialMinIdx,
                                onSelect = { selectedMinute = it % 60 },
                            )
                        }
                    }
                }

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

//            Spacer(Modifier.height(16.dp))

//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(AppTheme.shapes.medium)
//                    .padding(vertical = 8.dp),
//            ) {
//                AppCheckbox(
//                    checked = isAllDay,
//                    onToggle = { isAllDay = !isAllDay },
//                )
//                Spacer(Modifier.width(12.dp))
//                Text(
//                    "Весь день",
//                    style = AppTheme.types.body,
//                    color = AppTheme.colors.onSurface,
//                )
//            }

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
                        stringResource(R.string.done) + " · ${formatPreviewShort(current, today, isAllDay)}",
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
    items: List<String>,
    initialIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex,
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    val scope = rememberCoroutineScope()


    val centeredIndex by remember(items.size) {
        derivedStateOf {
            val info = state.layoutInfo
            val visible = info.visibleItemsInfo
            if (visible.isEmpty()) {
                initialIndex.coerceIn(0, items.lastIndex)
            } else {
                val viewportCenter =
                    (info.viewportStartOffset + info.viewportEndOffset) / 2
                visible.minBy { item ->
                    val itemCenter = item.offset + item.size / 2
                    kotlin.math.abs(itemCenter - viewportCenter)
                }.index.coerceIn(0, items.lastIndex)
            }
        }
    }

    LaunchedEffect(state, items.size) {
        snapshotFlow { state.isScrollInProgress to centeredIndex }
            .distinctUntilChanged()
            .collect { (scrolling, idx) ->
                if (!scrolling) {
                    onSelect(idx.coerceIn(0, items.lastIndex))
                }
            }
    }

    LazyColumn(
        state = state,
        flingBehavior = flingBehavior,
        modifier = modifier.fillMaxHeight(),
        contentPadding = PaddingValues(vertical = WHEEL_ROW_HEIGHT * 2),
    ) {
        items(items.size) { i ->
            val distance = kotlin.math.abs(i - centeredIndex)
            val alpha = when (distance) {
                0 -> 1f
                1 -> 0.55f
                2 -> 0.28f
                else -> 0.15f
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WHEEL_ROW_HEIGHT)
                    .clickable { scope.launch { state.animateScrollToItem(i) } }
                    .alpha(alpha),
            ) {
                Text(
                    text = items[i],
                    style = if (distance == 0) AppTheme.types.titleLarge else AppTheme.types.title,
                    color = if (distance == 0) AppTheme.colors.onSurface
                    else AppTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun formatDateLabel(date: LocalDate, today: LocalDate): String {
    return when (date) {
        today -> stringResource(R.string.today)
        today.plus(1, DateTimeUnit.DAY) -> stringResource(R.string.tomorrow)
        today.minus(1, DateTimeUnit.DAY) -> stringResource(R.string.yesterday)
        else -> {
            val javaDate = java.time.LocalDate.of(date.year, date.month.number, date.day)
            val weekday = javaDate.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.SHORT, java.util.Locale.getDefault(),
            ).replaceFirstChar { it.titlecase(java.util.Locale.getDefault()) }
            val month = javaDate.month.getDisplayName(
                java.time.format.TextStyle.SHORT,
                java.util.Locale.getDefault(),
            ).trimEnd('.')
            "$weekday, ${date.dayOfMonth} $month"
        }
    }
}

@Composable
private fun formatPreview(dt: LocalDateTime, today: LocalDate, isAllDay: Boolean): String {
    val datePart = when (dt.date) {
        today -> stringResource(R.string.today)
        today.plus(1, DateTimeUnit.DAY) -> stringResource(R.string.tomorrow)
        else -> {
            val javaDate = java.time.LocalDate.of(dt.year, dt.month.number, dt.day)
            javaDate.format(
                java.time.format.DateTimeFormatter.ofPattern("d MMMM, EEEE"),
            )
        }
    }
    return if (isAllDay) {
        "$datePart · ${stringResource(R.string.all_day)}"
    } else {
        val h = dt.hour.toString().padStart(2, '0')
        val m = dt.minute.toString().padStart(2, '0')
        "$datePart · $h:$m"
    }
}

@Composable
private fun formatPreviewShort(dt: LocalDateTime, today: LocalDate, isAllDay: Boolean): String {
    val datePart = when (dt.date) {
        today -> stringResource(R.string.today)
        today.plus(1, DateTimeUnit.DAY) -> stringResource(R.string.tomorrow)
        else -> {
            val javaDate = java.time.LocalDate.of(dt.year, dt.month.number, dt.day)
            javaDate.format(
                java.time.format.DateTimeFormatter.ofPattern("d MMM"),
            )
        }
    }
    return if (isAllDay) datePart else {
        val h = dt.hour.toString().padStart(2, '0')
        val m = dt.minute.toString().padStart(2, '0')
        "$datePart, $h:$m"
    }
}