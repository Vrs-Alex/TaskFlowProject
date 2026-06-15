package com.vrsalex.taskflow.presentation.feature.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.LoginData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.presentation.common.message.AppMessenger
import com.vrsalex.taskflow.presentation.common.message.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val messenger: AppMessenger,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInContract.State())
    val state = _state.asStateFlow()

    private val _channel = Channel<SignInContract.Effect>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SignInContract.Event) {
        when (event) {
            is SignInContract.Event.FieldChanged -> when (event.field) {
                SignInContract.Field.Identity -> _state.update { it.copy(identity = event.value) }
                SignInContract.Field.Password -> _state.update { it.copy(password = event.value) }
            }
            SignInContract.Event.Submit -> submit()
            SignInContract.Event.SignUp -> viewModelScope.launch {
                _channel.send(SignInContract.Effect.OnSignUp)
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val result = authRepository.login(
            LoginData(identity = state.value.identity.trim(), password = state.value.password)
        )
        _state.update { it.copy(isLoading = false) }
        when (result) {
            is Resource.Success -> _channel.send(SignInContract.Effect.OnSignIn)
            Resource.Error.NoInternet -> messenger.error(UiText.Raw("Нет подключения к интернету"))
            Resource.Error.ServerError -> messenger.error(UiText.Raw("Ошибка сервера, попробуйте позже"))
            is Resource.Error.HttpError -> messenger.error(UiText.Raw(result.msg))
        }
    }
}
