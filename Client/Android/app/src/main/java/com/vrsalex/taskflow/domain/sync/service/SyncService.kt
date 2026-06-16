package com.vrsalex.taskflow.domain.sync.service

import kotlinx.coroutines.flow.StateFlow

interface SyncService {

    fun connect()

    val isConnected: StateFlow<Boolean>

}