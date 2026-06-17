package com.vrsalex.taskflow.presentation.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.domain.common.start.GetStartDestinationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppEntryViewModel(
    private val getStartDestinationUseCase: GetStartDestinationUseCase,
    private val authObserver: AuthObserver
): ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination = _startDestination.asStateFlow()


    init {
        viewModelScope.launch {
            val d = getStartDestinationUseCase()
            _startDestination.update { d }

            if (d == MainGraph) {
                authObserver.setAuthorized(true)
            }
        }
    }


}