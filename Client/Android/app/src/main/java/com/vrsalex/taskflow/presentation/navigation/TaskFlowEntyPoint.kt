package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.vrsalex.uikit.theme.AppTheme
import com.vrsalex.uikit.theme.TaskFlowTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskFlowEntyPoint(
    viewModel: EntyPointViewModel = koinViewModel()
) {
    TaskFlowTheme(isDarkTheme = true) {
        val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
        val navController = rememberNavController()
        AppNavHost(navController, startDestination)

    }

}