package com.vrsalex.taskflow.data.item.note

import com.vrsalex.network.public.api.item.NoteApi
import com.vrsalex.taskflow.data.item.base.ItemSyncEngine
import com.vrsalex.taskflow.data.item.base.RemoteSync
import com.vrsalex.taskflow.data.item.base.toCreateDto
import com.vrsalex.taskflow.data.item.base.toDomain
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.item.base.toUpdateDto
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.NoteLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.note.NoteRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteRepositoryImpl(
    noteApi: NoteApi,
    private val noteLocalDataSource: NoteLocalDataSource,
    private val itemLocalDataSource: ItemLocalDataSource,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : NoteRepository {

    private val engine = ItemSyncEngine(
        syncEntity = SyncDbEntity.NOTE,
        api = noteApi,
        itemLocalDataSource = itemLocalDataSource,
        outboxHandler = outboxHandler,
        syncHandler = syncHandler,
        remote = object : RemoteSync<ItemDto, ItemCreateRequest, ItemUpdateRequest> {
            override suspend fun applyRemote(dto: ItemDto) {
                itemLocalDataSource.insert(dto.toEntityWithRelations())
            }

            override suspend fun buildCreate(id: Uuid): ItemCreateRequest? =
                itemLocalDataSource.getItemRelationByIdRaw(id)?.toDomain()?.toCreateDto()

            override suspend fun buildUpdate(id: Uuid): ItemUpdateRequest? =
                itemLocalDataSource.getItemRelationByIdRaw(id)?.toDomain()?.toUpdateDto()
        },
    )

    override fun get(): Flow<List<Item>> =
        noteLocalDataSource.getNotes().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Item?> =
        noteLocalDataSource.getNote(id).map { it?.toDomain() }

    override suspend fun create(data: ItemCreate) {
        itemLocalDataSource.insert(data.toEntityWithRelations())
        engine.enqueueCreate(data.id)
    }

    override suspend fun update(data: ItemUpdate) {
        itemLocalDataSource.update(data)
        engine.enqueueUpdate(data.id)
    }

    override suspend fun delete(id: Uuid) = engine.delete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = engine.sync(lastSync)

    override suspend fun syncItem(id: Uuid): Resource<Unit> = engine.syncItem(id)
}
