package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.ForgotPassword
import com.vrsalex.taskflow.presentation.navigation.LoginDestination
import com.vrsalex.taskflow.presentation.navigation.RegisterDestination

fun NavGraphBuilder.authGraph(navController: NavController){

    navigation<AuthGraph>(
        startDestination = LoginDestination
    ){

        composable<LoginDestination> {  }


        composable<RegisterDestination> {  }


        composable<ForgotPassword> {  }

    }

}