package vrsalex.event.web

import vrsalex.core.model.toOptional
import vrsalex.event.domain.Event
import vrsalex.event.domain.EventCreate
import vrsalex.event.domain.EventUpdate
import vrsalex.item.domain.model.ItemType
import vrsalex.item.web.toDomain
import vrsalex.item.web.toDto
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventItemUpdateRequest

fun EventCreateRequest.toDomain() = EventCreate(
    base = this.base.toDomain(ItemType.EVENT),
    startDate = this.startDate,
    endDate = this.endDate,
    location = this.location
)


fun EventItemUpdateRequest.toDomain() = EventUpdate(
    base = this.base.toDomain(),
    startDate = this.startDate.toOptional(),
    endDate = this.endDate.toOptional(),
    location = this.location.toOptional()
)

fun Event.toDto() = EventDto(
    base = this.base.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
    location = this.location
)