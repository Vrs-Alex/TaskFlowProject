package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.navigation.graph.authGraph
import com.vrsalex.taskflow.presentation.navigation.graph.mainGraph
import com.vrsalex.taskflow.presentation.navigation.graph.onBoardingGraph
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppEntry(
    viewModel: AppEntryViewModel = koinViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    startDestination?.let { destination ->
        NavHost(
            navController = navController,
            startDestination = destination
        ){
            onBoardingGraph(navController)
            authGraph(navController)
            mainGraph()
        }
    }
}