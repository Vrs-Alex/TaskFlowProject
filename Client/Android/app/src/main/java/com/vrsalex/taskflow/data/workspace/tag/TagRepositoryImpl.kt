package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.network.public.api.TagApi
import com.vrsalex.taskflow.data.local.db.datasource.TagLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.taskflow.domain.workscape.tag.TagCreate
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import com.vrsalex.taskflow.domain.workscape.tag.TagUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TagRepositoryImpl(
    private val tagApi: TagApi,
    private val tagLocalDataSource: TagLocalDataSource,
    private val syncHandler: SyncHandler,
    private val outboxHandler: OutboxHandler
) : TagRepository {

    init {
        outboxHandler.register(SyncDbEntity.TAG, object : OutboxEntityHandler {
            override suspend fun create(id: Uuid): Resource<SyncModel> {
                val tag = tagLocalDataSource.getByIdRaw(id)
                    ?: return Resource.Error("Tag not found")
                return tagApi.create(tag.toDomain().toCreateDto()).toResource { it.toSyncModel() }
            }
            override suspend fun update(id: Uuid): Resource<SyncModel> {
                val tag = tagLocalDataSource.getByIdRaw(id)
                    ?: return Resource.Error("Tag not found")
                return tagApi.update(tag.toDomain().toUpdateDto()).toResource { it.toSyncModel() }
            }
            override suspend fun delete(id: Uuid): Resource<Unit> {
                val tag = tagLocalDataSource.getByIdRaw(id)
                    ?: return Resource.Error("Tag not found")
                val serverId = tag.serverId
                    ?: return Resource.Error("ServerId not found")
                return tagApi.delete(tag.id, serverId, tag.version).toResource { tagLocalDataSource.delete(id) }
            }

            override suspend fun markAsSynced(
                id: Uuid,
                syncModel: SyncModel
            ) {
                tagLocalDataSource.markSynced(
                    id = id,
                    newId = syncModel.id,
                    serverId = syncModel.serverId ?: return,
                    version = syncModel.version,
                    updatedAt = syncModel.updatedAt,
                )
            }

            override suspend fun findExisting(itemId: Uuid): SyncModel? {
                val local = tagLocalDataSource.getByIdRaw(itemId) ?: return null
                val result = tagApi.findByFilter(name = local.name, nameExact = true)
                    .toResource { it.firstOrNull()?.toSyncModel() }
                return (result as? Resource.Success)?.data
            }
        })
    }

    override fun get(): Flow<List<Tag>> =
        tagLocalDataSource.getTags().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Tag?> =
        tagLocalDataSource.getTag(id).map { it?.toDomain() }

    override suspend fun create(data: TagCreate) {
        tagLocalDataSource.insert(data.toEntity())
        outboxHandler.addOperation(data.id, SyncDbEntity.TAG, PendingOperation.CREATE)
    }

    override suspend fun update(data: TagUpdate) {
        tagLocalDataSource.update(data)
        outboxHandler.addOperation(data.id, SyncDbEntity.TAG, PendingOperation.UPDATE)
    }

    override suspend fun delete(id: Uuid) {
        val tag = tagLocalDataSource.getByIdRaw(id)
        if (tag?.serverId == null) {
            tagLocalDataSource.delete(id)
            return
        }
        tagLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, SyncDbEntity.TAG, PendingOperation.DELETE)
    }

    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.TAG,
            lastSync = lastSync,
            fetch = tagApi::get,
            insert = { tagLocalDataSource.insert(it.toEntity()) },
            delete = tagLocalDataSource::delete,
            getLocalSyncableModel = { dto ->
                tagLocalDataSource.getByIdRaw(dto.clientId)
            }
        )
}