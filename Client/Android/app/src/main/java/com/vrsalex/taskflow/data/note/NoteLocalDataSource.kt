package com.vrsalex.taskflow.data.note

import androidx.room.withTransaction
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.entity.NoteTagCrossRef
import com.vrsalex.taskflow.data.local.db.relation.NoteRelation
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import com.vrsalex.taskflow.domain.workspace.area.AreaScope
import vrsalex.shared.api.item.base.ItemDto
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class NoteLocalDataSource(private val db: AppDatabase) {

    private val dao get() = db.noteDao()

    fun observeAll(): Flow<List<NoteRelation>> = dao.observeAll()
    fun observe(id: Uuid): Flow<NoteRelation?> = dao.observe(id)
    suspend fun getRaw(id: Uuid) = dao.getRaw(id)

    suspend fun create(data: NoteCreate) = db.withTransaction {
        dao.upsertWithTags(data.toEntity(), data.tagIds())
    }

    suspend fun upsertFromRemote(dto: ItemDto) = db.withTransaction {
        dao.upsertWithTags(dto.toEntity(), dto.tags)
    }

    suspend fun update(data: NoteUpdate) = db.withTransaction {
        val current = dao.getRaw(data.syncModelUpdate.id) ?: return@withTransaction
        val updated = data.applyTo(current)
        val tagIds = data.tagIdsOrNull()
        if (tagIds != null) dao.upsertWithTags(updated, tagIds) else dao.upsert(updated)
    }

    suspend fun softDelete(id: Uuid) = dao.softDelete(id)
    suspend fun delete(id: Uuid) = dao.delete(id)


    fun observeWithFilters(areaScope: AreaScope, areaId: Uuid?, status: NoteStatus?, query: String) =
        dao.observeWithFilters(areaScope.name, areaId, status?.name, query)

}
