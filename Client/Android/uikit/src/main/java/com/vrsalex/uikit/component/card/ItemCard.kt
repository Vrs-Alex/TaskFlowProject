package com.vrsalex.uikit.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.theme.AppTheme

enum class ItemCardType { Event, Task, Goal, Habit }

data class ItemTypeStyle(
    val hue: Color,
    val soft: Color,
    val border: Color,
    val icon: ImageVector
)

@Composable
private fun getItemTypeStyle(type: ItemCardType): ItemTypeStyle = with(AppTheme.typeColors) {
    when (type) {
        ItemCardType.Event -> ItemTypeStyle(event, eventSoft, eventBorder, ImageVector.vectorResource(R.drawable.calendar))
        ItemCardType.Task  -> ItemTypeStyle(task, taskSoft, taskBorder, ImageVector.vectorResource(R.drawable.calendar))
        ItemCardType.Goal  -> ItemTypeStyle(goal, goalSoft, goalBorder, ImageVector.vectorResource(R.drawable.calendar))
        ItemCardType.Habit -> ItemTypeStyle(habit, habitSoft, habitBorder, ImageVector.vectorResource(R.drawable.calendar))
    }
}

@Composable
fun ItemTypeBadge(type: ItemCardType, modifier: Modifier = Modifier) {
    val s = getItemTypeStyle(type)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(32.dp)
            .background(s.soft, AppTheme.shapes.medium)
            .border(1.dp, s.border, AppTheme.shapes.medium),
    ) {
        Icon(s.icon, contentDescription = null, tint = s.hue, modifier = Modifier.size(17.dp))
    }
}

@Composable
fun ItemCard(
    type: ItemCardType,
    title: String,
    subline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    areaName: String? = null,
    areaColor: Color? = null,
    tags: List<Pair<String, Color?>> = emptyList(),
    synced: Boolean = true,
    syncIcon: ImageVector? = ImageVector.vectorResource(R.drawable.cloud),
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(indication = ripple(), interactionSource = interactionSource) { onClick() }
            .background(AppTheme.colors.surface, AppTheme.shapes.large)
            .border(1.dp, AppTheme.colors.outline, AppTheme.shapes.large),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ItemTypeBadge(type = type)
                        Text(
                            title,
                            style = AppTheme.types.title,
                            color = AppTheme.colors.onSurface,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!synced && syncIcon != null) {
                            Icon(
                                imageVector = syncIcon,
                                contentDescription = "Не синхр.",
                                tint = AppTheme.colors.warning,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                    subline()
                }

                action?.invoke()
            }

            if (areaName != null || tags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    if (areaName != null && areaColor != null) {
                        AppChip(text = areaName, color = areaColor.copy(alpha = 0.75f), filled = true)
                    }
                    tags.take(3).forEach { (name, c) ->
                        AppChip(text = "# $name", color = c?.copy(alpha = 0.75f) ?: AppTheme.colors.onSurface, filled = false)
                    }
                }
            }
        }
    }
}

