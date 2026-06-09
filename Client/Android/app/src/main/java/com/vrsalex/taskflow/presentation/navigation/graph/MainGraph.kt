package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vrsalex.taskflow.presentation.feature.shell.AppShell
import com.vrsalex.taskflow.presentation.navigation.MainGraph


fun NavGraphBuilder.mainGraph(navController: NavController) {

    composable<MainGraph> {
        AppShell()
    }
}
