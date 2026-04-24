package com.vrsalex.taskflow.data.sync

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import vrsalex.shared.api.common.ModelDto
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

class SyncHandler(private val syncRepository: SyncRepository) {

    suspend fun <T> sync(
        syncEntity: SyncDbEntity,
        lastSync: Instant?,
        fetch: suspend (Instant?) -> NetworkResult<List<ModelDto<T>>>,
        insert: suspend (T) -> Unit,
        delete: suspend (Uuid) -> Unit
    ): Resource<Unit> {
        val resolvedLastSync = lastSync ?: syncRepository.getLastSync(syncEntity)
        return fetch(resolvedLastSync).toResource { data ->
            syncRepository.setLastSync(syncEntity, Clock.System.now())
            data.forEach { model ->
                when (model) {
                    is ModelDto.Active -> insert(model.data)
                    is ModelDto.Deleted -> delete(model.clientId)
                }
            }
        }
    }
}