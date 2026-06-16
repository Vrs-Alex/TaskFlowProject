package com.vrsalex.taskflow.domain.note.event

import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

interface EventRepository : SyncRepository<Event, EventCreate, EventUpdate> {
    fun observeByDate(date: Instant): Flow<List<Event>>
    fun observeByDateRange(start: LocalDate, end: LocalDate): Flow<List<Event>>
    fun observeArchived(query: String): Flow<List<Event>>
}