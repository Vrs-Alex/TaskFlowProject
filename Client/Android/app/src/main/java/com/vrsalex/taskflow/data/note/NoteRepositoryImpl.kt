package com.vrsalex.taskflow.data.note

import com.vrsalex.network.public.api.item.NoteApi
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.data.sync.SyncPusher
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import com.vrsalex.taskflow.domain.workspace.area.AreaScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteRepositoryImpl(
    private val local: NoteLocalDataSource,
    private val syncPuller: SyncPuller,
    private val syncPusher: SyncPusher,
    private val noteApi: NoteApi
) : NoteRepository {

    override fun observeAll(): Flow<List<Note>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Note?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: NoteCreate) = local.create(data)
    override suspend fun update(data: NoteUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncPuller.sync(
            syncEntity = SyncEntity.NOTE,
            lastSync = lastSync,
            fetch = { since -> noteApi.sync(since) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { id -> local.delete(id) },
            getLocalSyncModelColumns = { id -> local.getRaw(id) }
        )

    override suspend fun syncById(id: Uuid): Resource<Unit> =
        syncPuller.syncItem(
            id = id,
            fetchItem = { noteApi.syncItem(it) },
            upsert = { dto -> local.upsertFromRemote(dto) },
            delete = { local.delete(it) },
        )

    override suspend fun push(): Resource<Unit> =
        syncPusher.push(
            getDirty = { local.getDirty() },
            getId = { it.note.id },
            getSync = { it.note.sync },
            create = { noteApi.create(it.toCreateRequest()) },
            update = { noteApi.update(it.toUpdateRequest()) },
            delete = { id, serverId, version -> noteApi.delete(id, serverId, version) },
            upsertFromRemote = { dto -> local.upsertFromRemote(dto) },
            hardDelete = { id -> local.delete(id) },
            pullItem = { id -> syncById(id) },
        )


    override fun observeWithFilters(areaScope: AreaScope, areaId: Uuid?, status: NoteStatus?, query: String): Flow<List<Note>> =
        local.observeWithFilters(areaScope, areaId, status, query).map { list -> list.map { it.toDomain() } }
}
