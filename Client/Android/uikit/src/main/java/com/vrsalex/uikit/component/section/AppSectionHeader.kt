package com.vrsalex.uikit.component.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme


@Composable
fun AppSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    count: Int? = null,
    accentColor: Color? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(
            horizontal = 12.dp,
            vertical = 10.dp,
        ),
    ) {
        accentColor?.let {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color = it)
            )
        }

        Text(
            title.uppercase(),
            style = AppTheme.types.caption,
            color = AppTheme.colors.onSurfaceVariant,
        )
        if (count != null) {
            Text(
                count.toString(),
                style = AppTheme.types.micro,
                color = AppTheme.colors.onSurfaceMuted,
            )
        }
    }
}