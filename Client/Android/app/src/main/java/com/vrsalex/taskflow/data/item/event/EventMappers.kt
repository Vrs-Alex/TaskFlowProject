package com.vrsalex.taskflow.data.item.event

import com.vrsalex.taskflow.data.item.base.toDomain
import com.vrsalex.taskflow.data.item.base.toDto
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.relation.EventWithItem
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.event.Event
import com.vrsalex.taskflow.domain.item.event.EventCreate
import com.vrsalex.taskflow.domain.item.event.EventUpdate
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventUpdateRequest

fun EventDto.toDomain() = Event(
    base = this.base.toDomain(),
    startDate = this.startDate,
    endDate = this.endDate,
    location = this.location
)

fun EventCreate.toDto() = EventCreateRequest(
    base = this.base.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
)

fun EventUpdate.toDto() = EventUpdateRequest(
    base = this.base.toDto(),
    startDate = this.startDate.toOptionalDto(),
    endDate = this.endDate.toOptionalDto(),
    location = this.location.toOptionalDto()
)


fun EventWithItem.toDomain() = Event(
    base = item.toDomain(),
    startDate = event.startDate,
    endDate = event.endDate,
    location = event.location
)

fun EventDto.toEntity() = EventEntity(
    itemId = this.base.clientId,
    startDate = this.startDate,
    endDate = this.endDate,
    location = this.location
)