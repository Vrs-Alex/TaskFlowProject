package com.vrsalex.taskflow.presentation.feature.calendar.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.presentation.common.extension.monthNameRes
import com.vrsalex.taskflow.presentation.common.extension.weekdayShortRes
import com.vrsalex.taskflow.presentation.feature.calendar.CalendarContract
import com.vrsalex.uikit.component.background.AppBlurBackground
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

object CalendarHeaderDefaults {
    // Приблизительная полная высота свёрнутой шапки (вкл. status bar) — фолбэк до первого замера.
    // Близка к реальной, чтобы свести к минимуму подскок списка на первом кадре.
    val CollapsedHeightFallback = 180.dp
    val ExpandedExtra = 220.dp
}

@Composable
fun CalendarHeader(
    state: CalendarContract.State,
    currentDate: LocalDate,
    headerRowState: LazyListState,
    hazeState: HazeState,
    onDateClick: (LocalDate) -> Unit,
    onAction: (CalendarContract.Action) -> Unit,
    onTodayClick: () -> Unit,
    onCollapsedHeight: (Dp) -> Unit,
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val progress = remember { Animatable(0f) }

    var morphRangePx by remember { mutableIntStateOf(0) }
    val rangePx = rememberUpdatedState(morphRangePx)

    val velocityTracker = remember { VelocityTracker() }

    LaunchedEffect(state.calendarState) {
        val target = if (state.calendarState == CalendarContract.CalendarState.COLLAPSED) 0f else 1f
        if (progress.value != target) {
            progress.animateTo(target, animationSpec = tween(300))
        }
    }

    AppBlurBackground(hazeState, color = AppTheme.colors.background.copy(alpha = 0.9f)) {
        Column(
            modifier.fillMaxWidth()
                // ВЫШЕ statusBarsPadding — чтобы замер включал высоту статус-бара (полный футпринт шапки).
                .onSizeChanged { size ->
                    if (progress.value == 0f) onCollapsedHeight(with(density) { size.height.toDp() })
                }
                .dropShadow(
                    shape = RoundedCornerShape(0.dp),
                    shadow = Shadow(
                        radius = 14.dp,
                        spread = 0.dp,
                        offset = DpOffset(x = 0.dp, y = 3.dp),
                        color = AppTheme.colors.scrim.copy(alpha = 0.15f),
                    )
                )
                .statusBarsPadding()
                .pointerInput(state.calendarState) {
                    detectVerticalDragGestures(
                        onDragStart = {
                            velocityTracker.resetTracking()
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            velocityTracker.addPosition(change.uptimeMillis, change.position)

                            val range = rangePx.value
                            if (range > 0) {
                                val next = (progress.value + dragAmount / range).coerceIn(0f, 1f)
                                scope.launch { progress.snapTo(next) }
                            }
                        },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().y

                            scope.launch {
                                val target = when {
                                    velocity > 500f -> 1f
                                    velocity < -500f -> 0f
                                    progress.value > 0.5f -> 1f
                                    else -> 0f
                                }

                                progress.animateTo(target, animationSpec = tween(durationMillis = 250))

                                val finalState = if (target == 0f) {
                                    CalendarContract.CalendarState.COLLAPSED
                                } else {
                                    CalendarContract.CalendarState.EXPANDED
                                }
                                if (finalState != state.calendarState) {
                                    onAction(CalendarContract.Action.ChangeCalendarState(finalState))
                                }
                            }
                        }
                    )
                }
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "${stringResource(currentDate.month.monthNameRes())} ${currentDate.year}",
                    style = AppTheme.types.title,
                    color = AppTheme.colors.onSurface
                )
                Spacer(Modifier.width(8.dp))
                AnimatedVisibility(currentDate != state.currentDate) {
                    AppIcon(
                        icon = R.drawable.today,
                        onClick = onTodayClick,
                        modifier = Modifier.size(24.dp),
                        tint = AppTheme.colors.onSurface
                    )
                }
            }
            MorphingArea(
                progress = progress.value,
                expandedExtraPx = with(density) { CalendarHeaderDefaults.ExpandedExtra.roundToPx() },
                onRangeMeasured = { morphRangePx = it },
                modifier = Modifier.fillMaxWidth(),
                collapsed = {
                    CalendarHeaderCollapse(
                        state = state,
                        currentDate = currentDate,
                        headerRowState = headerRowState,
                        onDateClick = onDateClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                expanded = {
                    CalendarHeaderExpand(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier.width(48.dp).height(4.dp).clip(CircleShape)
                        .background(AppTheme.colors.onSurfaceMuted)
                )
            }
        }
    }
}


@Composable
private fun MorphingArea(
    progress: Float,
    expandedExtraPx: Int,
    onRangeMeasured: (rangePx: Int) -> Unit,
    collapsed: @Composable () -> Unit,
    expanded: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    SubcomposeLayout(modifier) { constraints ->
        val loose = constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)

        val collapsedPlaceables = subcompose(MorphSlot.Collapsed, collapsed).map { it.measure(loose) }
        val expandedPlaceables = subcompose(MorphSlot.Expanded, expanded).map { it.measure(loose) }

        val collapsedH = collapsedPlaceables.maxOfOrNull { it.height } ?: 0
        val measuredExpandedH = expandedPlaceables.maxOfOrNull { it.height } ?: 0
        val expandedH =
            if (measuredExpandedH > collapsedH) measuredExpandedH else collapsedH + expandedExtraPx

        onRangeMeasured(expandedH - collapsedH)

        val height = lerp(collapsedH, expandedH, progress)
        layout(constraints.maxWidth, height) {
            if (progress < 1f) collapsedPlaceables.forEach {
                it.placeRelativeWithLayer(0, 0) { alpha = 1f - progress }
            }
            if (progress > 0f) expandedPlaceables.forEach {
                it.placeRelativeWithLayer(0, 0) { alpha = progress }
            }
        }
    }
}

private enum class MorphSlot { Collapsed, Expanded }

@Composable
private fun CalendarHeaderCollapse(
    state: CalendarContract.State,
    currentDate: LocalDate,
    headerRowState: LazyListState,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        LazyRow(
            state = headerRowState,
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = state.calendarState == CalendarContract.CalendarState.COLLAPSED
        ) {
            items(state.days, key = { it.date.toString() }) { day ->
                val isCurrent = day.date == currentDate
                val (backgroundColor, textColor) = getDateColors(isCurrent)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        Modifier
                            .size(42.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .drawBehind {
                                drawCircle(color = backgroundColor)
                            }
                            .clickable(interactionSource = null, indication = ripple()) {
                                onDateClick(day.date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.date.day.toString(),
                            style = AppTheme.types.body,
                            color = textColor
                        )
                    }
                    WeekNameText(day.date.weekdayShortRes())
                }
            }
        }
    }
}

@Composable
private fun CalendarHeaderExpand(
    state: CalendarContract.State,
    onAction: (CalendarContract.Action) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {

    }
}


@Composable
private fun getDateColors(isCurrent: Boolean): Pair<Color, Color> {
    return if (isCurrent) {
        AppTheme.colors.primary to AppTheme.colors.onPrimary
    } else {
        AppTheme.colors.surface to AppTheme.colors.onSurfaceVariant
    }
}

@Composable
private fun WeekNameText(@StringRes weekNameRes: Int) {
    Text(
        text = stringResource(weekNameRes),
        style = AppTheme.types.label,
        color = AppTheme.colors.onSurfaceVariant
    )
}
