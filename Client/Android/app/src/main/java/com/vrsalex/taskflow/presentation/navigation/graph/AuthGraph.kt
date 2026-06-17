package com.vrsalex.taskflow.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vrsalex.taskflow.presentation.feature.auth.signin.SignInScreen
import com.vrsalex.taskflow.presentation.feature.auth.signup.SignUpScreen
import com.vrsalex.taskflow.presentation.navigation.AuthGraph
import com.vrsalex.taskflow.presentation.navigation.ForgotPasswordDestination
import com.vrsalex.taskflow.presentation.navigation.LoginDestination
import com.vrsalex.taskflow.presentation.navigation.MainGraph
import com.vrsalex.taskflow.presentation.navigation.RegisterDestination

fun NavGraphBuilder.authGraph(navController: NavController){

    navigation<AuthGraph>(
        startDestination = LoginDestination
    ){

        composable<LoginDestination> {
            SignInScreen(
                onSignIn = {
                    navController.navigate(MainGraph){
                        popUpTo(MainGraph)
                    }
                },
                onSignUp = {
                    navController.navigate(RegisterDestination)
                }
            )
        }


        composable<RegisterDestination> {
            SignUpScreen(
                onSignIn = {
                    navController.popBackStack()
                },
                onSignUp = {
                    navController.navigate(MainGraph){
                        popUpTo(MainGraph)
                    }
                }
            )
        }


        composable<ForgotPasswordDestination> {  }

    }

}