package com.vrsalex.taskflow.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrsalex.taskflow.domain.common.GetStartDestinationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EntyPointViewModel(
    private val getStartDestinationUseCase: GetStartDestinationUseCase
): ViewModel() {


    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            _startDestination.update { getStartDestinationUseCase() }
        }
    }

}