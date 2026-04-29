package com.vrsalex.taskflow.domain.realtime

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RealtimeService {

    fun observe()

    val isConnected: StateFlow<Boolean>

}