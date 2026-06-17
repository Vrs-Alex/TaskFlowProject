package com.vrsalex.taskflow.data.realtime

import com.vrsalex.taskflow.domain.realtime.RealtimeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RealtimeServiceImpl(): RealtimeService {

    override fun connect() {
        // TODO
        _isConnected.value = false
    }

    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

}