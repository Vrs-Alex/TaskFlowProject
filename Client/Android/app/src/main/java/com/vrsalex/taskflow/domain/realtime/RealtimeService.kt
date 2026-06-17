package com.vrsalex.taskflow.domain.realtime

import kotlinx.coroutines.flow.StateFlow

interface RealtimeService {

    fun connect()

    val isConnected: StateFlow<Boolean>



}