package com.vrsalex.taskflow.presentation.feature.add_note.task

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import com.vrsalex.taskflow.domain.utils.formatForChip
import com.vrsalex.taskflow.presentation.common.extension.getNoteTypeColor
import com.vrsalex.taskflow.presentation.common.extension.weekdayNarrowRes
import com.vrsalex.taskflow.presentation.feature.add_note.task.AddItemTaskContract.RecurrenceEnd
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.modal.AppBottomSheet
import com.vrsalex.uikit.component.time.AppDateTimePicker
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus

private const val DEFAULT_COUNT = 10

private val WEEK_DAYS = listOf(
    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY,
)

private val RECURRENCE_OPTIONS: List<Pair<RecurrenceType?, Int>> = listOf(
    null to R.string.recurrence_none,
    RecurrenceType.DAILY to R.string.recurrence_daily,
    RecurrenceType.WEEKLY to R.string.recurrence_weekly,
    RecurrenceType.MONTHLY to R.string.recurrence_monthly,
    RecurrenceType.YEARLY to R.string.recurrence_yearly,
)

/** Подпись для чипа повтора: тип повтора или «Повтор», если повтора нет. */
@StringRes
fun RecurrenceType?.labelRes(): Int = when (this) {
    null -> R.string.repeat
    RecurrenceType.DAILY -> R.string.recurrence_daily
    RecurrenceType.WEEKLY -> R.string.recurrence_weekly
    RecurrenceType.MONTHLY -> R.string.recurrence_monthly
    RecurrenceType.YEARLY -> R.string.recurrence_yearly
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceBottomSheet(
    dueDate: LocalDate,
    selectedType: RecurrenceType?,
    selectedDays: Set<DayOfWeek>,
    selectedEnd: RecurrenceEnd,
    onTypeChange: (RecurrenceType?) -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onEndChange: (RecurrenceEnd) -> Unit,
    onDismiss: () -> Unit,
) {
    val accent = getNoteTypeColor(NoteType.TASK)
    var showEndDatePicker by remember { mutableStateOf(false) }

    AppBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.repeat),
                style = AppTheme.types.titleLarge,
                color = AppTheme.colors.onSurface,
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                RECURRENCE_OPTIONS.forEach { (type, label) ->
                    AppChip(
                        text = stringResource(label),
                        color = accent,
                        filled = selectedType == type,
                        onClick = { onTypeChange(type) },
                    )
                }
            }

            AnimatedVisibility(visible = selectedType == RecurrenceType.WEEKLY) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    WEEK_DAYS.forEach { day ->
                        AppChip(
                            text = stringResource(day.weekdayNarrowRes()),
                            color = accent,
                            filled = day in selectedDays,
                            onClick = { onDayToggle(day) },
                        )
                    }
                }
            }

            // Окончание повтора — только когда повтор включён.
            AnimatedVisibility(visible = selectedType != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.recurrence_end),
                        style = AppTheme.types.label,
                        color = AppTheme.colors.onSurfaceVariant,
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        AppChip(
                            text = stringResource(R.string.recurrence_end_never),
                            color = accent,
                            filled = selectedEnd is RecurrenceEnd.Never,
                            onClick = { onEndChange(RecurrenceEnd.Never) },
                        )
                        AppChip(
                            text = stringResource(R.string.recurrence_end_on_date),
                            color = accent,
                            filled = selectedEnd is RecurrenceEnd.OnDate,
                            onClick = {
                                if (selectedEnd !is RecurrenceEnd.OnDate) {
                                    onEndChange(RecurrenceEnd.OnDate(dueDate.plus(DatePeriod(months = 1))))
                                }
                                showEndDatePicker = true
                            },
                        )
                        AppChip(
                            text = stringResource(R.string.recurrence_end_after),
                            color = accent,
                            filled = selectedEnd is RecurrenceEnd.AfterCount,
                            onClick = {
                                if (selectedEnd !is RecurrenceEnd.AfterCount) {
                                    onEndChange(RecurrenceEnd.AfterCount(DEFAULT_COUNT))
                                }
                            },
                        )
                    }

                    when (val end = selectedEnd) {
                        is RecurrenceEnd.OnDate -> AppChip(
                            text = LocalDateTime(end.date, LocalTime(0, 0)).formatForChip(true),
                            color = accent,
                            filled = true,
                            onClick = { showEndDatePicker = true },
                        )

                        is RecurrenceEnd.AfterCount -> CountStepper(
                            count = end.count,
                            onChange = { onEndChange(RecurrenceEnd.AfterCount(it)) },
                        )

                        RecurrenceEnd.Never -> Unit
                    }
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = AppTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primary,
                    contentColor = AppTheme.colors.onPrimary,
                ),
            ) {
                Text(stringResource(R.string.apply), style = AppTheme.types.button)
            }

            Spacer(Modifier.height(4.dp))
        }
    }

    val end = selectedEnd
    if (showEndDatePicker && end is RecurrenceEnd.OnDate) {
        AppDateTimePicker(
            initial = LocalDateTime(end.date, LocalTime(0, 0)),
            title = stringResource(R.string.end_date),
            initialAllDay = true,
            showTimeCheckbox = false,
            onConfirm = { picked ->
                onEndChange(RecurrenceEnd.OnDate(picked.date))
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false },
        )
    }
}

@Composable
private fun CountStepper(
    count: Int,
    onChange: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StepperButton(symbol = "−", enabled = count > 1) { onChange(count - 1) }
        Text(
            text = count.toString(),
            style = AppTheme.types.title,
            color = AppTheme.colors.onSurface,
        )
        StepperButton(symbol = "+", enabled = true) { onChange(count + 1) }
        Text(
            text = stringResource(R.string.recurrence_occurrences),
            style = AppTheme.types.label,
            color = AppTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun StepperButton(
    symbol: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .border(1.dp, AppTheme.colors.outline, CircleShape)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(symbol, style = AppTheme.types.title, color = AppTheme.colors.onSurface)
    }
}
