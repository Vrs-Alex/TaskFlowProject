package com.vrsalex.taskflow.data.note

import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteRepository
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import com.vrsalex.taskflow.domain.workspace.area.AreaScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import kotlin.uuid.Uuid

class NoteRepositoryImpl(
    private val local: NoteLocalDataSource,
) : NoteRepository {

    override fun observeAll(): Flow<List<Note>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Uuid): Flow<Note?> =
        local.observe(id).map { it?.toDomain() }

    override suspend fun create(data: NoteCreate) = local.create(data)
    override suspend fun update(data: NoteUpdate) = local.update(data)
    override suspend fun delete(id: Uuid) = local.softDelete(id)

    override suspend fun sync(lastSync: Instant?): Resource<Unit> = Resource.Success(Unit)
    override suspend fun syncById(id: Uuid): Resource<Unit> = Resource.Success(Unit)


    override fun observeWithFilters(areaScope: AreaScope, areaId: Uuid?, status: NoteStatus?, query: String): Flow<List<Note>> =
        local.observeWithFilters(areaScope, areaId, status, query).map { list -> list.map { it.toDomain() } }
}
