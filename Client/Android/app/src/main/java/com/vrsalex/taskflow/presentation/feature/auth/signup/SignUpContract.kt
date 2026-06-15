package com.vrsalex.taskflow.presentation.feature.auth.signup

import com.vrsalex.taskflow.R

object SignUpContract {


    data class State(
        val email: String = "",
        val username: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false
    ) {
        val isCredentialsValid: Boolean
            get() = email.isNotBlank() && username.isNotBlank()

        val isPasswordValid: Boolean
            get() = password.isNotBlank() &&
                    confirmPassword.isNotBlank() &&
                    password == confirmPassword

        val passwordError: Int? = if (confirmPassword.isBlank() || password == confirmPassword) null else R.string.password_not_match

        val isSubmitEnabled: Boolean
            get() = isPasswordValid && isCredentialsValid && !isLoading
    }

    sealed interface Event {
        data class FieldChanged(val field: Field, val value: String) : Event
        data object Submit : Event
        data object SignIn : Event
    }

    sealed interface Effect {
        data object OnSignUp : Effect
        data object OnSignIn : Effect
    }

    enum class Field {
        Email,
        Username,
        Password,
        ConfirmPassword
    }

}