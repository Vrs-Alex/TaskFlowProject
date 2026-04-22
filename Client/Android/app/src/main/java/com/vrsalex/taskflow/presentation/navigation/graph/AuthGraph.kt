package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.SignInDestination
import com.vrsalex.taskflow.presentation.navigation.SignUpDestination

fun NavGraphBuilder.authGraph(navController: NavController) {

    navigation<AuthGraph>(
        startDestination = SignInDestination
    ){

        composable<SignInDestination> {

        }

        composable<SignUpDestination> {

        }

    }

}