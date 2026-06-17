package com.vrsalex.taskflow.presentation.feature.inbox.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun EmptyInbox() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Входящие пусты",
            style = AppTheme.types.title,
            color = AppTheme.colors.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Здесь появляется то, что вы быстро записали и ещё не разобрали",
            style = AppTheme.types.bodyMedium,
            color = AppTheme.colors.onSurfaceMuted,
            textAlign = TextAlign.Center,
        )
    }
}
