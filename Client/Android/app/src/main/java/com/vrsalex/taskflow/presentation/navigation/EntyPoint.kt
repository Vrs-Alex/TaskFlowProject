package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.vrsalex.uikit.theme.TaskFlowTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntyPoint(
    viewModel: EntryPointViewModel = koinViewModel()
) {

    TaskFlowTheme(isDarkTheme = true) {
        val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
        val navController = rememberNavController()

        LaunchedEffect(Unit) {
            viewModel.authObserver.logoutObserver.collect {
                navController.navigate(AuthGraph){ popUpTo(0){ inclusive = true} }
            }
        }

        AppNavHost(navController, startDestination)

    }

}