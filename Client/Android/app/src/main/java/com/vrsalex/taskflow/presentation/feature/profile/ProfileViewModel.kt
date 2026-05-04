package com.vrsalex.taskflow.presentation.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.presentation.feature.profile.ProfileContract.Action
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authObserver: AuthObserver
): ViewModel() {

    // TODO: получать из хранилища (сохранять при signIn/signUp)
    private val _state = MutableStateFlow(ProfileContract.State(name = "", email = ""))
    val state = _state.asStateFlow()

    fun onAction(a: Action) {
        when (a) {
            Action.Logout -> viewModelScope.launch { authObserver.logout() }
        }
    }
}
