package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.taskflow.domain.common.model.Resource
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
) : TagRepository {

    override fun observeAll(): Flow<List<Tag>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Tag?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: TagCreate) = local.create(data)
    override suspend fun update(data: TagUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = Resource.Success(Unit)
    override suspend fun syncById(id: Uuid): Resource<Unit> = Resource.Success(Unit)
}
