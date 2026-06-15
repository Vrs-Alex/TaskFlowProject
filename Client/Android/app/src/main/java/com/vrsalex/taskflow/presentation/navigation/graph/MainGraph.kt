package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vrsalex.taskflow.presentation.navigation.BrowseDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.InboxDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination

fun NavGraphBuilder.mainGraph(){

    composable<MainGraph> {
        val navController = rememberNavController()
        val currentDestination by navController.currentBackStackEntryAsState()

        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = InboxDestination
            ) {

                composable<InboxDestination> {  }

                composable<CalendarDestination> {  }

                composable<BrowseDestination> {  }

                composable<ProfileDestination> {  }

            }
        }
    }

}