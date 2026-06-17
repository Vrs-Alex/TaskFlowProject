package com.vrsalex.taskflow.presentation.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.profile.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val profileRepository: ProfileRepository
): ViewModel() {


    val state: StateFlow<ProfileContract.State> = profileRepository.getProfile()
        .map { profile ->
            ProfileContract.State(
                name = profile.name,
                email = profile.email
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileContract.State()
        )

    fun onAction(action: ProfileContract.Action){
        when(action) {
            ProfileContract.Action.ProfileClicked -> TODO()
            ProfileContract.Action.SettingClicked -> TODO()
            ProfileContract.Action.StatisticClicked -> TODO()
        }
    }


}