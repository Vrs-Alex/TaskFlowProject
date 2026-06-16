package com.vrsalex.taskflow.data.sync

import com.vrsalex.taskflow.domain.sync.service.SyncService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SyncServiceImpl: SyncService {

    override fun connect() {
        // TODO
        _isConnected.value = false
    }

    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
}