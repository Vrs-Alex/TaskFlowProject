package com.vrsalex.taskflow.domain.note.base

import com.vrsalex.taskflow.domain.sync.SyncRepository
import com.vrsalex.taskflow.domain.workspace.area.AreaScope
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface NoteRepository : SyncRepository<Note, NoteCreate, NoteUpdate> {

    fun observeWithFilters(
        areaScope: AreaScope,
        areaId: Uuid?,
        status: NoteStatus?,
        query: String
    ): Flow<List<Note>>

    fun observeInbox() = observeWithFilters(
        areaScope = AreaScope.NONE,
        areaId = null,
        status = NoteStatus.ACTIVE,
        query = "",
    )
}