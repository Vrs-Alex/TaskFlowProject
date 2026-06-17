package com.vrsalex.taskflow.data.note.event

import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventRelation
import com.vrsalex.taskflow.data.note.toItemCreateRequest
import com.vrsalex.taskflow.data.note.toItemUpdateRequest
import com.vrsalex.taskflow.data.note.toNote
import com.vrsalex.taskflow.domain.note.event.Event
import com.vrsalex.taskflow.domain.note.event.EventCreate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest

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

fun EventDto.toEventEntity(): EventEntity = EventEntity(
    id = base.clientId,
    startDate = startDate,
    endDate = endDate,
    isAllDay = isAllDay,
    location = location,
)

// --- Локальная dirty-запись → запросы на сервер (push) ---

fun EventRelation.toCreateRequest(): EventCreateRequest = EventCreateRequest(
    base = note.toItemCreateRequest(tags.map { it.id }),
    startDate = event.startDate,
    endDate = event.endDate,
    isAllDay = event.isAllDay,
    location = event.location,
)

fun EventRelation.toUpdateRequest(): EventUpdateRequest = EventUpdateRequest(
    base = note.toItemUpdateRequest(tags.map { it.id }),
    startDate = OptionalFieldDto.Defined(event.startDate),
    endDate = OptionalFieldDto.Defined(event.endDate),
    isAllDay = OptionalFieldDto.Defined(event.isAllDay),
    location = OptionalFieldDto.Defined(event.location),
)
