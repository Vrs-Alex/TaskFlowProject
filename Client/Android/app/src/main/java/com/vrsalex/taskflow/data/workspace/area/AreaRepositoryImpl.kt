package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.taskflow.domain.common.model.Resource
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
) : AreaRepository {

    override fun observeAll(): Flow<List<Area>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Area?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: AreaCreate) = local.create(data)
    override suspend fun update(data: AreaUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = Resource.Success(Unit)
    override suspend fun syncById(id: Uuid): Resource<Unit> = Resource.Success(Unit)
}
