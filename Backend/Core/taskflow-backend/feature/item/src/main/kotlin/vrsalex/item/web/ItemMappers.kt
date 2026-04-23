package vrsalex.item.web

import vrsalex.core.model.toOptional
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.model.toItemStatus
import vrsalex.item.domain.model.toItemStatusDto
import vrsalex.item.domain.model.toItemTypeDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest


fun ItemCreateRequest.toDomain(type: ItemType) = ItemCreate(
    clientId = this.clientId,
    name = this.name,
    description = this.description,
    type = type,
    priority = this.priority,
    areaId = this.areaId
)

fun ItemUpdateRequest.toDomain() = ItemUpdate(
    clientId = this.clientId,
    id = this.id,
    version = this.version,
    name = this.name.toOptional(),
    description = this.description.toOptional(),
    status = this.status.toOptional().map { it.toItemStatus() },
    priority = this.priority.toOptional(),
    areaId = this.areaId.toOptional()
)


fun Item.toDto() = ItemDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
    name = this.name,
    description = this.description,
    status = this.status.toItemStatusDto(),
    type = this.type.toItemTypeDto(),
    priority = this.priority,
    areaId = this.areaId
)