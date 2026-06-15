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
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Тип-специфичная часть синхронизации. Репозиторий реализует ТОЛЬКО это (три метода),
 * остальное (outbox, retry, conflict, cursor, delete) берёт на себя [ItemSyncEngine].
 */
interface RemoteSync<TDto, TCreateReq, TUpdateReq> {
    /** Применить серверный DTO в локальную БД (upsert + reconcile extension-строк). */
    suspend fun applyRemote(dto: TDto)

    /** Загрузить локальную запись и собрать запрос на создание (для outbox). */
    suspend fun buildCreate(id: Uuid): TCreateReq?

    /** Загрузить локальную запись и собрать запрос на обновление (для outbox). */
    suspend fun buildUpdate(id: Uuid): TUpdateReq?
}

/**
 * Конкретный движок синхронизации одного типа item. Композиция вместо наследования:
 * репозиторий ДЕРЖИТ движок и отдаёт ему [RemoteSync], а не наследует базовый класс с хуками.
 *
 * Вся generic-механика (outbox-обработчик, enqueue, soft-delete, delta-sync) живёт здесь и
 * тестируется в изоляции.
 */
class ItemSyncEngine<TDto : SyncDto, TCreateReq, TUpdateReq>(
    private val syncEntity: SyncDbEntity,
    private val api: SyncApi<TDto, TCreateReq, TUpdateReq>,
    private val itemLocalDataSource: ItemLocalDataSource,
    private val outboxHandler: OutboxHandler,
    private val syncHandler: SyncHandler,
    private val remote: RemoteSync<TDto, TCreateReq, TUpdateReq>,
) {
    init {
        outboxHandler.register(syncEntity, object : OutboxEntityHandler {

            override suspend fun create(id: Uuid): Resource<SyncModel> {
                val request = remote.buildCreate(id)
                    ?: return Resource.Failure.Error("Item not found")
                return api.create(request).toResource { it.toSyncModel() }
            }

            override suspend fun update(id: Uuid): Resource<SyncModel> {
                val request = remote.buildUpdate(id)
                    ?: return Resource.Failure.Error("Item not found")
                return api.update(request).toResource { it.toSyncModel() }
            }

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

    fun enqueueCreate(id: Uuid) = outboxHandler.addOperation(id, syncEntity, PendingOperation.CREATE)

    fun enqueueUpdate(id: Uuid) = outboxHandler.addOperation(id, syncEntity, PendingOperation.UPDATE)

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
            insert = { remote.applyRemote(it) },
            delete = itemLocalDataSource::delete,
            getLocalSyncableModel = { dto -> itemLocalDataSource.getByIdRaw(dto.clientId) },
        )

    suspend fun syncItem(id: Uuid): Resource<Unit> =
        syncHandler.syncItem(
            id = id,
            fetchItem = api::syncItem,
            insert = { remote.applyRemote(it) },
            delete = itemLocalDataSource::delete,
        )
}
