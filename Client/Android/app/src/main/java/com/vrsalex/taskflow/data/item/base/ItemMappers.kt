package com.vrsalex.taskflow.data.item.base

import com.vrsalex.taskflow.data.workspace.area.toDomain
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.ItemWithTagsAndArea
import com.vrsalex.taskflow.data.workspace.tag.toDomain
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.base.toStatusDomain
import com.vrsalex.taskflow.domain.item.base.toStatusDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest


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
    areaId = this.area?.id,
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

fun ItemWithTagsAndArea.toDomain() = Item(
    id = item.id,
    serverId = item.serverId,
    updatedAt = item.updatedAt,
    version = item.version,
    createdAt = item.createdAt,
    isSynced = item.isSynced,
    name = item.name,
    description = item.description,
    status = item.status,
    type = item.type,
    priority = item.priority,
    tags = tags.map { it.toDomain() },
    area = area?.toDomain()
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

    tags = emptyList(),
    area = null
)