package com.vrsalex.taskflow.presentation.feature.add_note.task

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.R
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import com.vrsalex.taskflow.presentation.common.extension.getNoteTypeColor
import com.vrsalex.taskflow.presentation.common.extension.weekdayNarrowRes
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.modal.AppBottomSheet
import com.vrsalex.uikit.theme.AppTheme
import kotlinx.datetime.DayOfWeek

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
    selectedType: RecurrenceType?,
    selectedDays: Set<DayOfWeek>,
    onTypeChange: (RecurrenceType?) -> Unit,
    onDayToggle: (DayOfWeek) -> Unit,
    onDismiss: () -> Unit,
) {
    val accent = getNoteTypeColor(NoteType.TASK)

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
}
