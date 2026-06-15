package com.vrsalex.taskflow.presentation.feature.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.ui_messages.AppMessage.Notification
import com.vrsalex.taskflow.domain.common.ui_messages.AppMessenger
import com.vrsalex.taskflow.domain.common.ui_messages.MessageType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val appMessenger: AppMessenger
): ViewModel() {

    private val _state = MutableStateFlow(SignInContract.State())
    val state = _state.asStateFlow()

    private val _channel = Channel<SignInContract.Effect>()
    val channel = _channel.receiveAsFlow()


    fun onEvent(event: SignInContract.Event) {
        when(event){
            is SignInContract.Event.FieldChanged -> {
                when(event.field){
                    SignInContract.Field.Identity -> _state.update { it.copy(identity = event.value) }
                    SignInContract.Field.Password -> _state.update { it.copy(password = event.value) }
                }
            }
            SignInContract.Event.Submit -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val res = authRepository.signIn(state.value.identity, state.value.password)
                    _state.update { it.copy(isLoading = false) }
                    when (res) {
                        is Resource.Success -> _channel.send(SignInContract.Effect.OnSignIn)
                        is Resource.Failure.Unavailable ->
                            appMessenger.sendMessage(Notification("Нет подключения к интернету", MessageType.INFO))
                        is Resource.Failure ->
                            appMessenger.sendMessage(Notification(res.message, MessageType.INFO))
                    }
                }
            }

            SignInContract.Event.SignUp -> {
                viewModelScope.launch { _channel.send(SignInContract.Effect.OnSignUp) }
            }
        }
    }

}