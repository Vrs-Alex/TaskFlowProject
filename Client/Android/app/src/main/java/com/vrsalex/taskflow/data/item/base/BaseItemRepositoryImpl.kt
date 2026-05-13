package com.vrsalex.taskflow.data.item.base

import com.vrsalex.network.public.common.SyncApi
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import kotlinx.coroutines.flow.Flow
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

abstract class BaseItemRepositoryImpl<TDto : SyncDto, TCreate, TUpdate, TDomain>(
    private val syncEntity: SyncDbEntity,
    private val api: SyncApi<TDto, *, *>,
    protected val itemLocalDataSource: ItemLocalDataSource,
    protected val outboxHandler: OutboxHandler,
    private val syncHandler: SyncHandler,
) {
    init {
        outboxHandler.register(syncEntity, object : OutboxEntityHandler {

            override suspend fun create(id: Uuid): Resource<SyncModel> = outboxCreate(id)

            override suspend fun update(id: Uuid): Resource<SyncModel> = outboxUpdate(id)

            override suspend fun delete(id: Uuid): Resource<Unit> {
                val item = itemLocalDataSource.getByIdRaw(id)
                    ?: return Resource.Failure.Error("Item not found")
                val serverId = item.serverId
                    ?: return Resource.Failure.Error("ServerId not found")
                return api.delete(item.id, serverId, item.version)
                    .toResource { itemLocalDataSource.delete(id) }
            }

            override suspend fun markAsSynced(id: Uuid, syncModel: SyncModel) {
                itemLocalDataSource.markSynced(
                    id = id,
                    newId = syncModel.id,
                    serverId = syncModel.serverId ?: return,
                    version = syncModel.version,
                    updatedAt = syncModel.updatedAt,
                )
            }

            override suspend fun findExisting(itemId: Uuid): SyncModel? {
                val result = api.getById(itemId).toResource { it?.toSyncModel() }
                return (result as? Resource.Success)?.data
            }
        })
    }

    // --- Специфично для каждого типа ---
    protected abstract suspend fun outboxCreate(id: Uuid): Resource<SyncModel>
    protected abstract suspend fun outboxUpdate(id: Uuid): Resource<SyncModel>

    protected abstract suspend fun insert(dto: TDto)

    protected abstract suspend fun localInsert(data: TCreate): Uuid
    protected abstract suspend fun localUpdate(data: TUpdate): Uuid

    protected abstract fun observeAll(): Flow<List<TDomain>>
    protected abstract fun observeById(id: Uuid): Flow<TDomain?>


    fun get(): Flow<List<TDomain>> = observeAll()

    fun getById(id: Uuid): Flow<TDomain?> = observeById(id)

    suspend fun create(data: TCreate) {
        val id = localInsert(data)
        outboxHandler.addOperation(id, syncEntity, PendingOperation.CREATE)
    }

    suspend fun update(data: TUpdate) {
        val id = localUpdate(data)
        outboxHandler.addOperation(id, syncEntity, PendingOperation.UPDATE)
    }

    suspend fun delete(id: Uuid) {
        val item = itemLocalDataSource.getByIdRaw(id)
        if (item?.serverId == null) {
            itemLocalDataSource.delete(id)
            return
        }
        itemLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, syncEntity, PendingOperation.DELETE)
    }

    suspend fun sync(lastSync: Instant?): Resource<Unit> =
        syncHandler.sync(
            syncEntity = syncEntity,
            lastSync = lastSync,
            fetch = api::sync,
            insert = { insert(it) },
            delete = itemLocalDataSource::delete,
            getLocalSyncableModel = { dto -> itemLocalDataSource.getByIdRaw(dto.clientId) }
        )

    suspend fun syncItem(id: Uuid): Resource<Unit> =
        syncHandler.syncItem(
            id = id,
            fetchItem = api::syncItem,
            insert = { insert(it) },
            delete = itemLocalDataSource::delete
        )
}
