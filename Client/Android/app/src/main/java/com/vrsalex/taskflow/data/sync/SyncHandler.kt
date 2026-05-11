package com.vrsalex.taskflow.data.sync

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.taskflow.data.local.db.entity.sync.SyncDbModel
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class SyncHandler(private val syncRepository: SyncRepository) {

    suspend fun <T: SyncDto, R: SyncDbModel> sync(
        syncEntity: SyncDbEntity,
        lastSync: Instant?,
        fetch: suspend (Instant?) -> NetworkResult<List<ModelDto<T>>>,
        insert: suspend (T) -> Unit,
        delete: suspend (Uuid) -> Unit,
        getLocalSyncableModel: suspend (T) -> R?
    ): Resource<Unit> {
        val resolvedLastSync = lastSync ?: syncRepository.getLastSync(syncEntity)
        val syncTime = Clock.System.now().minus(5.seconds)

        return fetch(resolvedLastSync).toResource { data ->
            data.forEach { model ->
                when (model) {
                    is ModelDto.Active -> {
                        val local = getLocalSyncableModel(model.data)
                        if (local == null || (local.isSynced && local.version <= model.data.version)) {
                            insert(model.data)
                        }
                    }
                    is ModelDto.Deleted -> delete(model.clientId)
                }
            }
            syncRepository.setLastSync(syncEntity, syncTime)
        }
    }

    suspend fun <T: SyncDto> syncItem(
        id: Uuid,
        fetchItem: suspend (id: Uuid) -> NetworkResult<ModelDto<T>>,
        insert: suspend (T) -> Unit,
        delete: suspend (Uuid) -> Unit
    ): Resource<Unit> {
        return fetchItem(id).toResource { data ->
            when (data) {
                is ModelDto.Active -> {
                    insert(data.data)
                }
                is ModelDto.Deleted -> delete(data.clientId)
            }
        }
    }
}