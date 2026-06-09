package com.vrsalex.taskflow.presentation.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.domain.profile.ProfileRepository
import com.vrsalex.taskflow.presentation.feature.profile.ProfileContract.Action
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authObserver: AuthObserver,
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    init {
        observe()
    }

    fun observe(){
        combine(
            profileRepository.isPushEnabled(),
            profileRepository.isPushEnabled()
        ){ pushEnabled, pushEnabled2 ->
            ProfileContract.State(
                name = "",
                email = "",
                pushEnabled = pushEnabled
            )
        }
            .onEach { newState -> _state.update { newState } }
            .launchIn(viewModelScope)
    }

    fun onAction(a: Action) {
        when (a) {
            Action.Logout -> viewModelScope.launch { authObserver.logout() }
            is Action.ChangePushEnabled -> viewModelScope.launch { profileRepository.setPushEnabled(a.b) }
        }
    }
}
