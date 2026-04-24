package com.vrsalex.taskflow.data.realtime

import com.vrsalex.network.public.api.RealtimeApi
import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.network.public.provider.AuthObserver
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.domain.sync.SyncUseCase
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import vrsalex.shared.api.realtime.EntityTypeDto
import vrsalex.shared.api.realtime.RealtimeEventDto

class RealtimeServiceImpl(
    private val realtimeApi: RealtimeApi,
    private val syncUseCase: SyncUseCase,
    private val authObserver: AuthObserver
) : RealtimeService {

    private val scope = CoroutineScope(SupervisorJob())

    override fun observe() {
        scope.launch {
            authObserver.isAuthorized.collect { isAuthorized ->
                if (isAuthorized) realtimeApi.connect()
                else realtimeApi.disconnect()
            }
        }

        scope.launch {
            realtimeApi.messages.collect { event ->
                when (event) {
                    is NetworkResult.Success -> handleEvent(event.data)
                    else -> {}
                }
            }
        }
    }

    private suspend fun handleEvent(event: RealtimeEventDto) {
        when (event) {
            is RealtimeEventDto.EntityChanged -> {
                syncUseCase.syncEntity(
                    entity = event.entityType.toDbEntity(),
                    time = event.time
                )
            }
            is RealtimeEventDto.Notification -> {}
            RealtimeEventDto.Logout -> {}
        }
    }
}

private fun EntityTypeDto.toDbEntity() : SyncDbEntity = when(this) {
    EntityTypeDto.TAG -> SyncDbEntity.TAG
    EntityTypeDto.AREA -> SyncDbEntity.AREA
    EntityTypeDto.EVENT -> SyncDbEntity.EVENT
    EntityTypeDto.TASK -> SyncDbEntity.TASK
    EntityTypeDto.HABIT -> SyncDbEntity.HABIT
    EntityTypeDto.GOAL -> SyncDbEntity.GOAL
    EntityTypeDto.REMINDER -> SyncDbEntity.REMINDER
    EntityTypeDto.ATTACHMENT -> SyncDbEntity.ATTACHMENT
    EntityTypeDto.RECURRENCE -> SyncDbEntity.RECURRENCE
}
