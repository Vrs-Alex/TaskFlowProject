package com.vrsalex.taskflow.presentation.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.taskflow.presentation.model.note.field.PriorityUi
import com.vrsalex.uikit.theme.AppTheme

enum class ItemCardType { Note, Task, Event, Goal, Habit }

@Composable
private fun typeHue(type: ItemCardType): Color = with(AppTheme.typeColors) {
    when (type) {
        ItemCardType.Note -> note
        ItemCardType.Task -> task
        ItemCardType.Event -> event
        ItemCardType.Goal -> goal
        ItemCardType.Habit -> habit
    }
}

@Composable
fun ItemCard(
    type: ItemCardType?,
    title: String,
    subline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    priority: PriorityUi? = null,
    area: Pair<String, Color>? = null,
    tags: List<Pair<String, Color?>> = emptyList(),
    synced: Boolean = true,
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    titleContent: @Composable RowScope.() -> Unit = {
        Text(
            title,
            style = AppTheme.types.title,
            color = AppTheme.colors.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    },
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(AppTheme.shapes.medium)
            .clickable(indication = ripple(), interactionSource = interactionSource) { onClick() }
            .background(AppTheme.colors.surface)
            .border(1.dp, AppTheme.colors.outline, AppTheme.shapes.medium),
    ) {
        if (type != null) {
            Box(
                Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(typeHue(type)),
            )
        }

        Column(Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                titleContent()
                action?.invoke()
            }

            subline()

            if (priority != null || area != null || tags.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    priority?.let {
                        val name = stringResource(it.titleRes)
                        Box(
                            Modifier.clip(AppTheme.shapes.small)
                                .background(it.color.copy(alpha = 0.2f))
                                .padding(4.dp)
                        ) {
                            Text(
                                text = name.take(1) + name.takeLast(1),
                                style = AppTheme.types.label,
                                color = it.color,
                            )
                        }
                    }
                    if (area != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(area.second),
                            )
                            Text(
                                area.first,
                                style = AppTheme.types.label,
                                color = AppTheme.colors.onSurfaceVariant,
                            )
                        }
                    }
                    tags.take(3).forEach { (name, _) ->
                        Text(
                            "#$name",
                            style = AppTheme.types.label,
                            color = AppTheme.colors.onSurfaceMuted,
                        )
                    }
                }
            }
        }
    }
}
