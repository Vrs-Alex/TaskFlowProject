package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.taskflow.data.local.db.datasource.workspace.AreaLocalDataSource
import com.vrsalex.taskflow.data.sync.OutboxHandler
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.common.model.toResource
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.repository.OutboxEntityHandler
import com.vrsalex.taskflow.domain.sync.repository.toSyncModel
import com.vrsalex.taskflow.domain.workscape.area.Area
import com.vrsalex.taskflow.domain.workscape.area.AreaCreate
import com.vrsalex.taskflow.domain.workscape.area.AreaRepository
import com.vrsalex.taskflow.domain.workscape.area.AreaUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class AreaRepositoryImpl(
    private val areaApi: AreaApi,
    private val areaLocalDataSource: AreaLocalDataSource,
    private val syncHandler: SyncHandler,
    private val outboxHandler: OutboxHandler
) : AreaRepository {

    init {
        outboxHandler.register(
            SyncDbEntity.AREA,
            object : OutboxEntityHandler {
                override suspend fun create(id: Uuid): Resource<SyncModel> {
                    val area = areaLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Failure.Error("Area not found")
                    return areaApi.create(area.toDomain().toCreateDto())
                        .toResource { it.toSyncModel() }
                }

                override suspend fun update(id: Uuid): Resource<SyncModel> {
                    val area = areaLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Failure.Error("Area not found")
                    return areaApi.update(area.toDomain().toUpdateDto())
                        .toResource { it.toSyncModel() }
                }

                override suspend fun delete(id: Uuid): Resource<Unit> {
                    val area = areaLocalDataSource.getByIdRaw(id)
                        ?: return Resource.Failure.Error("Area not found")
                    val serverId = area.serverId
                        ?: return Resource.Failure.Error("ServerId not found")
                    return areaApi.delete(area.id, serverId, area.version)
                        .toResource { areaLocalDataSource.delete(id) }
                }

                override suspend fun markAsSynced(id: Uuid, syncModel: SyncModel) {
                    areaLocalDataSource.markSynced(
                        id,
                        syncModel.id,
                        syncModel.serverId ?: return,
                        syncModel.version,
                        syncModel.updatedAt
                    )
                }

                override suspend fun findExisting(itemId: Uuid): SyncModel? {
                    val local = areaLocalDataSource.getByIdRaw(itemId) ?: return null
                    val result = areaApi.findByFilter(name = local.name, nameExact = true)
                        .toResource { it.firstOrNull()?.toSyncModel() }
                    return (result as? Resource.Success)?.data
                }
            }
        )
    }

    override fun get(): Flow<List<Area>> =
        areaLocalDataSource.getAreas().map { list -> list.map { it.toDomain() } }

    override fun getById(id: Uuid): Flow<Area?> =
        areaLocalDataSource.getArea(id).map { it?.toDomain() }

    override suspend fun create(data: AreaCreate) {
        areaLocalDataSource.insert(data.toEntity())
        outboxHandler.addOperation(data.id, SyncDbEntity.AREA, PendingOperation.CREATE)
    }

    override suspend fun update(data: AreaUpdate) {
        areaLocalDataSource.update(data)
        outboxHandler.addOperation(data.id, SyncDbEntity.AREA, PendingOperation.UPDATE)
    }

    override suspend fun delete(id: Uuid) {
        val area = areaLocalDataSource.getByIdRaw(id)
        if (area?.serverId == null) {
            areaLocalDataSource.delete(id)
            return
        }
        areaLocalDataSource.softDelete(id)
        outboxHandler.addOperation(id, SyncDbEntity.AREA, PendingOperation.DELETE)
    }

    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.AREA,
            lastSync = lastSync,
            fetch = areaApi::sync,
            insert = { areaLocalDataSource.insert(it.toEntity()) },
            delete = areaLocalDataSource::delete,
            getLocalSyncableModel = { dto ->
                areaLocalDataSource.getByIdRaw(dto.clientId)
            }
        )

    override suspend fun syncItem(id: Uuid): Resource<Unit> =
        syncHandler.syncItem(
            id = id,
            fetchItem = areaApi::syncItem,
            insert = { areaLocalDataSource.insert(it.toEntity()) },
            delete = areaLocalDataSource::delete,
        )
}