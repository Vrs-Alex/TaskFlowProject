package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.vrsalex.taskflow.domain.common.AppMessage
import com.vrsalex.taskflow.domain.common.AppMessenger
import com.vrsalex.taskflow.presentation.common.snackbar.AppSnackBar
import com.vrsalex.taskflow.presentation.navigation.graph.authGraph
import com.vrsalex.taskflow.presentation.navigation.graph.mainGraph
import com.vrsalex.taskflow.presentation.navigation.graph.onBoardingGraph
import com.vrsalex.uikit.theme.AppTheme
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any? = null
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val appMessenger = koinInject<AppMessenger>()
    var currentNotification by remember { mutableStateOf<AppMessage.Notification?>(null) }

    LaunchedEffect(Unit) {
        appMessenger.messages.collect { message ->
            when (message) {
                is AppMessage.Notification -> {
                    currentNotification = message
                    snackBarHostState.showSnackbar(
                        message = message.message
                    )
                }
            }
        }
    }

    Scaffold(
        Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                currentNotification?.let { notification ->
                    AppSnackBar(
                        message = notification,
                        onClose = { data.dismiss() }
                    )
                }
            }
        }
    ) { padding ->

        if (startDestination == null) {

        } else {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                onBoardingGraph(navController)
                authGraph(navController)
                mainGraph(navController)
            }
        }
    }
}