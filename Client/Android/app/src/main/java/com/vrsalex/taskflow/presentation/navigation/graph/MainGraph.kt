package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrsalex.taskflow.presentation.navigation.ArchiveDestination
import com.vrsalex.taskflow.presentation.navigation.CalendarDestination
import com.vrsalex.taskflow.presentation.navigation.HomeDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.ProfileDestination

fun NavGraphBuilder.mainGraph(navController: NavController) {

    navigation<MainGraph>(
        startDestination = HomeDestination
    ){

        composable<HomeDestination> {

        }

        composable<ArchiveDestination> {

        }

        composable<CalendarDestination> {

        }

        composable<ProfileDestination> {

        }
    }


}