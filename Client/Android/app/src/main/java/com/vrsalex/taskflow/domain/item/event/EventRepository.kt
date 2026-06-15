package com.vrsalex.taskflow.domain.item.event

import com.vrsalex.taskflow.domain.item.base.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface EventRepository : ItemRepository<Event, EventCreate, EventUpdate> {

    fun getByDate(date: Instant): Flow<List<Event>>

    fun getArchived(query: String): Flow<List<Event>>
}
