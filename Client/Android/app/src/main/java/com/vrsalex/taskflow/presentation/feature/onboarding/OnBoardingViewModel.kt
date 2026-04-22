package com.vrsalex.taskflow.presentation.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel: ViewModel() {

    val state = MutableStateFlow(OnBoardingContract.state).asStateFlow()

    private val _channel = Channel<Unit>()
    val channel = _channel.receiveAsFlow()

    fun onNext() {
        viewModelScope.launch {
            _channel.send(Unit)
        }
    }

}