package com.vrsalex.taskflow.data.sync

import com.vrsalex.network.public.common.NetworkResult
import com.vrsalex.taskflow.data.local.db.entity.ISyncDbColumns
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.sync.repository.SyncCursorStore
import vrsalex.shared.api.common.ModelDto
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class SyncPuller(private val syncCursorStore: SyncCursorStore) {

    suspend fun <T: SyncDto, R: ISyncDbColumns> sync(
        syncEntity: SyncEntity,
        lastSync: Instant?,
        fetch: suspend (Instant?) -> NetworkResult<List<ModelDto<T>>>,
        upsert: suspend (T) -> Unit,
        delete: suspend (Uuid) -> Unit,
        getLocalSyncModelColumns: suspend (id: Uuid) -> R?
    ): Resource<Unit> {
        val resolvedLastSync = lastSync ?: syncCursorStore.getLastSync(syncEntity)
        val syncTime = Clock.System.now().minus(5.seconds)

        return fetch(resolvedLastSync).toResource { data ->
            data.forEach { model ->
                when (model) {
                    is ModelDto.Active -> {
                        val local = getLocalSyncModelColumns(model.data.clientId)
                        // Нет локально или оно синхронизировано и версией младше (исключает состояние неотправленных изменений)
                        if (local == null || (local.sync.isSynced && local.sync.version <= model.data.version)) {
                            upsert(model.data)
                        }
                    }
                    is ModelDto.Deleted -> delete(model.clientId)
                }
            }
            syncCursorStore.setLastSync(syncEntity, syncTime)
        }
    }

    suspend fun <T: SyncDto> syncItem(
        id: Uuid,
        fetchItem: suspend (id: Uuid) -> NetworkResult<ModelDto<T>>,
        upsert: suspend (T) -> Unit,
        delete: suspend (Uuid) -> Unit
    ): Resource<Unit> {
        return fetchItem(id).toResource { data ->
            when (data) {
                is ModelDto.Active -> {
                    upsert(data.data)
                }
                is ModelDto.Deleted -> delete(data.clientId)
            }
        }
    }
}