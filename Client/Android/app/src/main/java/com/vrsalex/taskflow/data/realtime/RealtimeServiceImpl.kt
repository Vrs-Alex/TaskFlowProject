package com.vrsalex.taskflow.data.realtime

import com.vrsalex.network.public.api.realtime.ConnectionState
import com.vrsalex.network.public.api.realtime.RealtimeApi
import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.sync.service.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vrsalex.shared.api.realtime.EntityTypeDto
import vrsalex.shared.api.realtime.RealtimeEventDto

class RealtimeServiceImpl(
    private val realtimeApi: RealtimeApi,
    private val syncService: SyncService,
    private val authObserver: AuthObserver
): RealtimeService {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun connect() {

        // Реакция на isAuthorized
        scope.launch {
            authObserver.isAuthorized.collect { isAuthorized ->
                if (isAuthorized) realtimeApi.connect()
                else realtimeApi.disconnect()
            }
        }

        // Реакция на события соединения WS
        scope.launch {
            realtimeApi.messages.collect { event ->
                when (event) {
                    is NetworkResult.Success -> handleEvent(event.data)
                    else -> {}
                }
            }
        }

        // Реакция на состояние подключения WS
        scope.launch {
            realtimeApi.connectionState.collect { state ->
                if (state == ConnectionState.CONNECTED) {
                    syncService.syncAll()
                    _isConnected.value = true
                } else {
                    _isConnected.value = false
                }
            }
        }
    }

    private suspend fun handleEvent(event: RealtimeEventDto) {
        when (event) {
            is RealtimeEventDto.EntityChanged -> {
                syncService.syncEntity(
                    entity = event.entityType.SyncEntity(),
                    time = event.time
                )
            }
            is RealtimeEventDto.Notification -> {}
            RealtimeEventDto.Logout -> {}
        }
    }

    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

}



private fun EntityTypeDto.SyncEntity() : SyncEntity = when(this) {
    EntityTypeDto.TAG -> SyncEntity.TAG
    EntityTypeDto.AREA -> SyncEntity.AREA
    EntityTypeDto.NOTE -> SyncEntity.NOTE
    EntityTypeDto.TASK -> SyncEntity.TASK
    EntityTypeDto.TASK_LOG -> SyncEntity.TASK_LOG
    EntityTypeDto.EVENT -> SyncEntity.EVENT
    EntityTypeDto.HABIT -> TODO()
    EntityTypeDto.GOAL -> TODO()
    EntityTypeDto.REMINDER -> TODO()
    EntityTypeDto.ATTACHMENT -> TODO()
}