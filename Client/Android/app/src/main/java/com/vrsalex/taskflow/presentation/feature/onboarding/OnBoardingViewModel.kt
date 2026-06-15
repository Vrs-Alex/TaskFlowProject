package com.vrsalex.taskflow.presentation.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.data.local.datastore.DataStoreManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    val state = MutableStateFlow(OnBoardingContract.state).asStateFlow()

    private val _channel = Channel<Unit>()
    val channel = _channel.receiveAsFlow()

    fun onNext() {
        viewModelScope.launch {
            dataStoreManager.setFirstLaunch(false)
            _channel.send(Unit)
        }
    }

}