package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.network.public.api.TagApi
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.workspace.tag.Tag
import com.vrsalex.taskflow.domain.workspace.tag.TagCreate
import com.vrsalex.taskflow.domain.workspace.tag.TagRepository
import com.vrsalex.taskflow.domain.workspace.tag.TagUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TagRepositoryImpl(
    private val local: TagLocalDataSource,
    private val syncPuller: SyncPuller,
    private val tagApi: TagApi
) : TagRepository {

    override fun observeAll(): Flow<List<Tag>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Tag?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: TagCreate) = local.create(data)
    override suspend fun update(data: TagUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncPuller.sync(
            syncEntity = SyncEntity.AREA,
            lastSync = lastSync,
            fetch = { since -> tagApi.sync(since) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { id -> local.delete(id) },
            getLocalSyncModelColumns = { id -> local.getRaw(id) }
        )

    override suspend fun syncById(id: Uuid): Resource<Unit> =
        syncPuller.syncItem(
            id = id,
            fetchItem = { tagApi.syncItem(it) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { local.delete(it) },
        )
}
