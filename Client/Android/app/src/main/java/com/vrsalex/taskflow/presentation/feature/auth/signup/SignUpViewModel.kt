package com.vrsalex.taskflow.presentation.feature.auth.signup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.auth.AuthRepository
import com.vrsalex.taskflow.domain.auth.RegisterData
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.presentation.common.message.AppMessenger
import com.vrsalex.taskflow.presentation.common.message.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val messenger: AppMessenger,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpContract.State())
    val state = _state.asStateFlow()

    private val _channel = Channel<SignUpContract.Effect>()
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.FieldChanged -> when (event.field) {
                SignUpContract.Field.Email -> _state.update { it.copy(email = event.value) }
                SignUpContract.Field.Username -> _state.update { it.copy(username = event.value) }
                SignUpContract.Field.Password -> _state.update { it.copy(password = event.value) }
                SignUpContract.Field.ConfirmPassword -> _state.update { it.copy(confirmPassword = event.value) }
            }
            SignUpContract.Event.Submit -> submit()
            SignUpContract.Event.SignIn -> viewModelScope.launch {
                _channel.send(SignUpContract.Effect.OnSignIn)
            }
        }
    }

    private fun submit() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }
        val result = authRepository.register(
            RegisterData(
                username = state.value.username.trim(),
                email = state.value.email.trim(),
                fullName = null,
                password = state.value.password,
            )
        )
        _state.update { it.copy(isLoading = false) }
        when (result) {
            is Resource.Success -> _channel.send(SignUpContract.Effect.OnSignUp)
            Resource.Error.NoInternet -> messenger.error(UiText.Raw("Нет подключения к интернету"))
            Resource.Error.ServerError -> messenger.error(UiText.Raw("Ошибка сервера, попробуйте позже"))
            is Resource.Error.HttpError -> messenger.error(UiText.Raw(result.msg))
        }
    }
}
