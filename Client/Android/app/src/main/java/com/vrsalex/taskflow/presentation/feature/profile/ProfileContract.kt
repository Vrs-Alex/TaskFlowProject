package com.vrsalex.taskflow.presentation.feature.profile

object ProfileContract {


    data class State(
        val name: String = "",
        val email: String = "",
        val pushEnabled: Boolean = false
    )

    sealed interface Action {
        data object Logout: Action
        data class ChangePushEnabled(val b: Boolean): Action
    }



}