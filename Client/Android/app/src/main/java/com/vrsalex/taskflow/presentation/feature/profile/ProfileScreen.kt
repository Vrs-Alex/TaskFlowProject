package com.vrsalex.taskflow.presentation.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileContent(state = state, onAction = viewModel::onAction)
}

@Composable
private fun ProfileContent(
    state: ProfileContract.State,
    onAction: (ProfileContract.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        AccountCard(name = state.name, email = state.email)

        StatsCard()

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            onClick = { onAction(ProfileContract.Action.Logout) },
            text = "Выйти из аккаунта",
            state = AppButtonState.Medium,
            backgroundColor = AppTheme.colors.error
        )
    }
}

@Composable
private fun AccountCard(name: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surfaceElevated, AppTheme.shapes.large)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(AppTheme.colors.primarySoft, AppTheme.shapes.round),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                style = AppTheme.types.title,
                color = AppTheme.colors.primary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = name.ifEmpty { "—" },
                style = AppTheme.types.title,
                color = AppTheme.colors.onSurface
            )
            Text(
                text = email.ifEmpty { "—" },
                style = AppTheme.types.bodyMedium,
                color = AppTheme.colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatsCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colors.surfaceElevated, AppTheme.shapes.large)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(value = "0", label = "Задачи")
        StatItem(value = "0", label = "События")
        StatItem(value = "0", label = "Области")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = AppTheme.types.displayMedium,
            color = AppTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = AppTheme.types.label,
            color = AppTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
