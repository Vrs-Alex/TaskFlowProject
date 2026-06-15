package com.vrsalex.taskflow.presentation.feature.auth.signin

object SignInContract {

    data class State(
        val identity: String = "",
        val password: String = "",
        val isLoading: Boolean = false
    ){
        val isEnabled: Boolean = identity.isNotEmpty() && password.isNotEmpty() && !isLoading
    }

    sealed interface Event {
        data class FieldChanged(val field: Field, val value: String) : Event
        data object Submit : Event
        data object SignUp : Event
    }

    enum class Field {
        Identity,
        Password
    }

    sealed interface Effect {
        data object OnSignUp: Effect
        data object OnSignIn: Effect
    }

}