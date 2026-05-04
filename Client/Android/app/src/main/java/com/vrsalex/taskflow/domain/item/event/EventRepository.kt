package com.vrsalex.taskflow.domain.item.event

import com.vrsalex.taskflow.domain.item.base.ItemRepository
import kotlinx.coroutines.flow.Flow

interface EventRepository : ItemRepository<Event, EventCreate, EventUpdate> {

    fun getArchived(query: String = ""): Flow<List<Event>>

}