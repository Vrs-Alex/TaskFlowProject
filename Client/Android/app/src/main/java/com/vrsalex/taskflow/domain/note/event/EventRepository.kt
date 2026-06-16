package com.vrsalex.taskflow.domain.note.event

import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface EventRepository : SyncRepository<Event, EventCreate, EventUpdate> {
    fun observeByDate(date: Instant): Flow<List<Event>>
    fun observeArchived(query: String): Flow<List<Event>>
}