package com.vrsalex.uikit.component.controller.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.vrsalex.uikit.R
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun <T> AppChipMenu(
    selected: T,
    items: List<T>,
    itemText: (T) -> String,
    itemColor: @Composable (T) -> Color,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var expanded by remember { mutableStateOf(false) }
    var chipHeight by remember { mutableIntStateOf(0) }

    Box(modifier = modifier) {
        AppChip(
            text = itemText(selected),
            color = itemColor(selected),
            filled = true,
            onClick = { expanded = true }
        )

        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false),
                offset = IntOffset(0, (chipHeight + with(density) { 8.dp.roundToPx() }))
            ) {
                Column(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppTheme.colors.surfaceElevated)
                        .padding(vertical = 8.dp)
                        .widthIn(max = 170.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = ripple(),
                                    interactionSource = null
                                ) {
                                    onItemSelected(item)
                                    expanded = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(itemColor(item), CircleShape)
                            )
                            Text(
                                text = itemText(item),
                                style = AppTheme.types.label,
                                color = if (item == selected)
                                    itemColor(item)
                                else AppTheme.colors.onSurface
                            )
                            if (item == selected) {
                                Spacer(Modifier.weight(1f))
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.check),
                                    contentDescription = null,
                                    tint = itemColor(item),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}