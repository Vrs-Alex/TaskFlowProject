package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.common.message.AppMessenger
import com.vrsalex.taskflow.presentation.common.message.asString
import com.vrsalex.taskflow.presentation.navigation.graph.authGraph
import com.vrsalex.taskflow.presentation.navigation.graph.mainGraph
import com.vrsalex.taskflow.presentation.navigation.graph.onBoardingGraph
import com.vrsalex.uikit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AppEntry(
    viewModel: AppEntryViewModel = koinViewModel(),
    messenger: AppMessenger = koinInject(),
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        messenger.messages.collect { msg ->
            snackbarHostState.showSnackbar(msg.text.asString(context), duration = SnackbarDuration.Short)
        }
    }

    Box(Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        startDestination?.let { destination ->
            NavHost(
                navController = navController,
                startDestination = destination,
                modifier = Modifier.fillMaxSize()
            ) {
                onBoardingGraph(navController)
                authGraph(navController)
                mainGraph()
            }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter).systemBarsPadding())
    }
}