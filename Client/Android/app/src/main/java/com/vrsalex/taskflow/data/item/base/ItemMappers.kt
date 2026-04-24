package com.vrsalex.taskflow.data.item.base

import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.base.toStatusDomain
import com.vrsalex.taskflow.domain.item.base.toStatusDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest


fun ItemDto.toDomain() = Item(
    serverId = this.id,
    id = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
    isSynced = true,
    name = this.name,
    description = this.description,
    status = this.status.toStatusDomain(),
    type = this.type.toStatusDomain(),
    priority = this.priority,
    areaId = this.areaId,
    tags = emptyList()
)

fun ItemCreate.toDto() = ItemCreateRequest(
    clientId = this.id,
    name = this.name,
    description = this.description,
    priority = this.priority,
    areaId = this.areaId,
    tags = this.tagIds,
)

fun ItemUpdate.toDto() = ItemUpdateRequest(
    clientId = this.id,
    id = this.serverId,
    version = this.version,
    name = this.name.toOptionalDto(),
    description = this.description.toOptionalDto(),
    status = this.status.map { it.toStatusDto() }.toOptionalDto(),
    priority = this.priority.toOptionalDto(),
    areaId = this.areaId.toOptionalDto(),
    tags = this.tagIds.toOptionalDto(),
)



fun ItemEntity.toDomain() = Item(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    name = this.name,
    description = this.description,
    status = this.status,
    type = this.type,
    priority = this.priority,
    areaId = this.areaId,
    tags = emptyList()
)

fun Item.toEntity() = ItemEntity(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    name = this.name,
    description = this.description,
    status = this.status,
    type = this.type,
    priority = this.priority,
    areaId = this.areaId,
)

fun ItemDto.toEntity() = ItemEntity(
    id = this.clientId,
    serverId = this.id,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = true,
    name = this.name,
    description = this.description,
    status = this.status.toStatusDomain(),
    type = this.type.toStatusDomain(),
    priority = this.priority,
    areaId = this.areaId
)