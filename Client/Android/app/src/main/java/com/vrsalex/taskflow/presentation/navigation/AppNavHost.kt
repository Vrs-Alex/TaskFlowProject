package com.vrsalex.taskflow.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.vrsalex.taskflow.presentation.navigation.graph.authGraph
import com.vrsalex.taskflow.presentation.navigation.graph.mainGraph
import com.vrsalex.taskflow.presentation.navigation.graph.onBoardingGraph
import com.vrsalex.uikit.theme.AppTheme

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any? = null
) {
    Column(Modifier.fillMaxSize().background(AppTheme.colors.background)) {

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