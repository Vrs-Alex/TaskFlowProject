package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vrsalex.taskflow.presentation.feature.onboarding.OnBoardingScreen
import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.OnBoardingDestination

fun NavGraphBuilder.onBoardingGraph(navController: NavController){


    composable<OnBoardingDestination> {
        OnBoardingScreen(
            onNext = {
                navController.navigate(AuthGraph){
                    popUpTo(AuthGraph)
                }
            }
        )
    }

}