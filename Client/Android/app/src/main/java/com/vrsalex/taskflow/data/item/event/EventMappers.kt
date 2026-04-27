package com.vrsalex.taskflow.data.item.event

import com.vrsalex.taskflow.data.item.base.toCreateDto
import com.vrsalex.taskflow.data.item.base.toDomain
import com.vrsalex.taskflow.data.item.base.toDto
import com.vrsalex.taskflow.data.item.base.toEntity
import com.vrsalex.taskflow.data.item.base.toUpdateDto
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItemTagsAndArea
import com.vrsalex.taskflow.data.workspace.area.toDomain
import com.vrsalex.taskflow.data.workspace.tag.toDomain
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest

// TO DTO

fun EventCreate.toDto() = EventCreateRequest(
    base = this.base.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
    isAllDay = this.isAllDay,
    location = this.location
)

fun EventUpdate.toDto() = EventUpdateRequest(
    base = this.base.toDto(),
    startDate = this.startDate.toOptionalDto(),
    endDate = this.endDate.toOptionalDto(),
    isAllDay = this.isAllDay.toOptionalDto(),
    location = this.location.toOptionalDto()
)

fun Event.toCreateDto() = EventCreateRequest(
    base = base.toCreateDto(),
    startDate = startDate,
    endDate = endDate,
    isAllDay = isAllDay,
    location = location
)

fun Event.toUpdateDto() = EventUpdateRequest(
    base = base.toUpdateDto(),
    startDate = OptionalFieldDto.Defined(startDate),
    endDate = OptionalFieldDto.Defined(endDate),
    isAllDay = OptionalFieldDto.Defined(isAllDay),
    location = OptionalFieldDto.Defined(location)
)

// TO DOMAIN

fun EventWithItemTagsAndArea.toDomain() = Event(
    base = item.toDomain().copy(
        tags = tags.map { it.toDomain() },
        area = area?.toDomain()
    ),
    startDate = event.startDate,
    endDate = event.endDate,
    isAllDay = event.isAllDay,
    location = event.location
)

// TO ENTITY

fun EventDto.toEntity() = EventEntity(
    itemId = this.base.clientId,
    startDate = this.startDate,
    endDate = this.endDate,
    isAllDay = this.isAllDay,
    location = this.location
)

fun EventCreate.toEntity() = EventEntity(
    itemId = base.id,
    startDate = startDate,
    endDate = endDate,
    isAllDay = isAllDay,
    location = location
)