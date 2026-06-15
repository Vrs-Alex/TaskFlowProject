package com.vrsalex.taskflow.data.note.event

import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import com.vrsalex.taskflow.data.note.toNote
import com.vrsalex.taskflow.domain.note.event.Event
import com.vrsalex.taskflow.domain.note.event.EventCreate

fun EventRelation.toDomain(): Event = Event(
    startDate = event.startDate,
    endDate = event.endDate,
    isAllDay = event.isAllDay,
    location = event.location,
    note = note.toNote(area, tags),
)

fun EventCreate.toEventEntity(): EventEntity = EventEntity(
    id = note.syncModelCreate.id,
    startDate = startDate,
    endDate = endDate,
    isAllDay = isAllDay,
    location = location,
)
