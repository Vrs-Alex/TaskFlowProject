package com.vrsalex.taskflow.data.item.note

import com.vrsalex.network.public.api.item.NoteApi
import com.vrsalex.taskflow.data.item.base.BaseItemRepositoryImpl
import com.vrsalex.taskflow.data.item.base.toCreateDto
import com.vrsalex.taskflow.data.item.base.toDomain
import com.vrsalex.taskflow.data.item.base.toEntityWithRelations
import com.vrsalex.taskflow.data.item.base.toUpdateDto
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.NoteLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.note.NoteRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vrsalex.shared.api.item.base.ItemDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteRepositoryImpl(
    private val noteApi: NoteApi,
    private val noteLocalDataSource: NoteLocalDataSource,
    itemLocalDataSource: ItemLocalDataSource,
    syncHandler: SyncHandler,
    outboxHandler: OutboxHandler,
) : BaseItemRepositoryImpl<ItemDto, ItemCreate, ItemUpdate, Item>(
    syncEntity = SyncDbEntity.NOTE,
    api = noteApi,
    itemLocalDataSource = itemLocalDataSource,
    outboxHandler = outboxHandler,
    syncHandler = syncHandler,
), NoteRepository {

    override suspend fun outboxCreate(id: Uuid): Resource<SyncModel> {
        val item = itemLocalDataSource.getItemRelationByIdRaw(id)
            ?: return Resource.Failure.Error("Note not found")
        return noteApi.create(item.toDomain().toCreateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun outboxUpdate(id: Uuid): Resource<SyncModel> {
        val item = itemLocalDataSource.getItemRelationByIdRaw(id)
            ?: return Resource.Failure.Error("Note not found")
        return noteApi.update(item.toDomain().toUpdateDto()).toResource { it.toSyncModel() }
    }

    override suspend fun insert(dto: ItemDto) {
        itemLocalDataSource.insert(dto.toEntityWithRelations())
    }

    override suspend fun localInsert(data: ItemCreate): Uuid {
        itemLocalDataSource.insert(data.toEntityWithRelations())
        return data.id
    }

    override suspend fun localUpdate(data: ItemUpdate): Uuid {
        itemLocalDataSource.update(data)
        return data.id
    }

    override fun observeAll(): Flow<List<Item>> =
        noteLocalDataSource.getNotes().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Item?> =
        noteLocalDataSource.getNote(id).map { it?.toDomain() }

    override fun getByDate(date: Instant): Flow<List<Item>> = observeAll()
}
