package com.vrsalex.taskflow.presentation.feature.auth.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.SignUpData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.notify.AppMessage.Notification
import com.vrsalex.taskflow.domain.common.notify.AppMessenger
import com.vrsalex.taskflow.domain.common.notify.MessageType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val appMessenger: AppMessenger
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpContract.State())
    val state = _state.asStateFlow()

    private val _channel = Channel<SignUpContract.Effect>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.FieldChanged -> {
                when (event.field) {
                    SignUpContract.Field.Email -> _state.update { it.copy(email = event.value) }
                    SignUpContract.Field.Username -> _state.update { it.copy(username = event.value) }
                    SignUpContract.Field.Password -> _state.update { it.copy(password = event.value) }
                    SignUpContract.Field.ConfirmPassword -> _state.update { it.copy(confirmPassword = event.value) }
                }
            }
            SignUpContract.Event.Submit -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val res = authRepository.signUp(
                        SignUpData(
                            email = state.value.email,
                            username = state.value.username,
                            password = state.value.password ,
                            confirmPassword = state.value.confirmPassword,
                            fullName = null
                        )
                    )
                    _state.update { it.copy(isLoading = false) }
                    when (res) {
                        is Resource.Success -> _channel.send(SignUpContract.Effect.OnSignUp)
                        is Resource.Failure.Unavailable ->
                            appMessenger.sendMessage(Notification("Нет подключения к интернету", MessageType.INFO))
                        is Resource.Failure ->
                            appMessenger.sendMessage(Notification(res.message, MessageType.INFO))
                    }
                }
            }

            SignUpContract.Event.SignIn -> { viewModelScope.launch { _channel.send(SignUpContract.Effect.OnSignIn) } }
        }
    }
}