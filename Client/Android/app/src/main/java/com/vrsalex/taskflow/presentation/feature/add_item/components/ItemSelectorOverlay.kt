package com.vrsalex.taskflow.presentation.feature.add_item.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.R
import com.vrsalex.uikit.component.controller.chip.AppChip
import com.vrsalex.uikit.component.icon.AppIcon
import com.vrsalex.uikit.theme.AppTheme
import kotlin.uuid.Uuid

@Composable
fun ItemSelectorOverlay(
    visible: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddClick: (name: String) -> Unit,
    items: List<SelectorItem>,
    selectedIds: Set<Uuid>,
    onItemClick: (SelectorItem) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + expandVertically(tween(250), expandFrom = Alignment.Bottom),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(200), shrinkTowards = Alignment.Bottom)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(AppTheme.colors.surfaceElevated)
                .pointerInput(Unit) { detectTapGestures { } }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(AppTheme.colors.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    cursorBrush = SolidColor(AppTheme.colors.primary),
                    textStyle = AppTheme.types.bodyMedium.copy(color = AppTheme.colors.onSurface),
                    decorationBox = { inner ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        ) {
                            Box(Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = stringResource(com.vrsalex.taskflow.R.string.search),
                                        style = AppTheme.types.bodyMedium,
                                        color = AppTheme.colors.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                                inner()
                            }
                            if (searchQuery.isNotEmpty() && !items.equals(searchQuery)) {
                                AppIcon(
                                    icon = R.drawable.add,
                                    onClick = { onAddClick(searchQuery) },
                                    modifier = Modifier.size(20.dp),
                                    tint = AppTheme.colors.onSurfaceVariant
                                )
                            }
                        }
                    }
                )

                Spacer(Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items, key = { it.id }) { item ->
                        val isSelected = item.id in selectedIds
                        AppChip(
                            text = item.name,
                            color = item.color,
                            filled = isSelected,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}