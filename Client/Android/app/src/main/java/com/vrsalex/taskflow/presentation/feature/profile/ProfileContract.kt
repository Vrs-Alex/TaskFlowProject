package com.vrsalex.taskflow.presentation.feature.profile

object ProfileContract {


    data class State(
        val name: String,
        val email: String
    )

    sealed interface Action {
        data object Logout: Action
    }



}