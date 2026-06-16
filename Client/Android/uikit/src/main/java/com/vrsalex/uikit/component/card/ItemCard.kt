package com.vrsalex.uikit.component.card

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
    areaName: String? = null,
    areaColor: Color? = null,
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
            .clip(AppTheme.shapes.small)
            .clickable(indication = ripple(), interactionSource = interactionSource) { onClick() }
            .background(AppTheme.colors.surface)
            .border(1.dp, AppTheme.colors.outline, AppTheme.shapes.small),
    ) {

        Column(Modifier.weight(1f).padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                titleContent()
                action?.invoke()
            }

            subline()

            if (areaName != null || tags.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    if (areaName != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(areaColor ?: AppTheme.colors.onSurfaceVariant),
                            )
                            Text(
                                areaName,
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
