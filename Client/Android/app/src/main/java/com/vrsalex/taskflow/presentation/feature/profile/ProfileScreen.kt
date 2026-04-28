package com.vrsalex.taskflow.presentation.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.uikit.component.button.AppButton
import com.vrsalex.uikit.component.button.AppButtonState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ProfileScreen() {

    val authObserver = koinInject<AuthObserver>()
    val scope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppButton(
            onClick = {
                scope.launch {
                    authObserver.logout()
                }
            },
            text = "Logout",
            state = AppButtonState.Medium
        )
    }

}