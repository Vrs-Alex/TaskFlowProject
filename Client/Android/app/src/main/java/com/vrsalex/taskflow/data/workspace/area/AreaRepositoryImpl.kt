package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.data.sync.SyncPusher
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.workspace.area.Area
import com.vrsalex.taskflow.domain.workspace.area.AreaCreate
import com.vrsalex.taskflow.domain.workspace.area.AreaRepository
import com.vrsalex.taskflow.domain.workspace.area.AreaUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class AreaRepositoryImpl(
    private val local: AreaLocalDataSource,
    private val syncPuller: SyncPuller,
    private val syncPusher: SyncPusher,
    private val areaApi: AreaApi
) : AreaRepository {

    override fun observeAll(): Flow<List<Area>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Area?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: AreaCreate) = local.create(data)
    override suspend fun update(data: AreaUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncPuller.sync(
            syncEntity = SyncEntity.AREA,
            lastSync = lastSync,
            fetch = { since -> areaApi.sync(since) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { id -> local.delete(id) },
            getLocalSyncModelColumns = { id -> local.getRaw(id) }
        )

    override suspend fun syncById(id: Uuid): Resource<Unit> =
        syncPuller.syncItem(
            id = id,
            fetchItem = { areaApi.syncItem(it) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { local.delete(it) },
        )

    override suspend fun push(): Resource<Unit> =
        syncPusher.push(
            getDirty = { local.getDirty() },
            getId = { it.id },
            getSync = { it.sync },
            create = { areaApi.create(it.toCreateRequest()) },
            update = { areaApi.update(it.toUpdateRequest()) },
            delete = { id, serverId, version -> areaApi.delete(id, serverId, version) },
            upsertFromRemote = { dto -> local.upsertFromRemote(dto) },
            hardDelete = { id -> local.delete(id) },
            pullItem = { id -> syncById(id) },
        )
}
