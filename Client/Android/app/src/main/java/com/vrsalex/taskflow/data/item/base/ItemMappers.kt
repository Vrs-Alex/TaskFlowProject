package com.vrsalex.taskflow.data.item.base

import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import com.vrsalex.taskflow.data.local.db.relation.ItemRelation
import com.vrsalex.taskflow.data.local.db.relation.ItemWithRelations
import com.vrsalex.taskflow.data.workspace.area.toDomain
import com.vrsalex.taskflow.data.workspace.tag.toDomain
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.item.base.Item
import com.vrsalex.taskflow.domain.item.base.ItemCreate
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import com.vrsalex.taskflow.domain.item.base.ItemUpdate
import com.vrsalex.taskflow.domain.item.base.toDomain
import com.vrsalex.taskflow.domain.item.base.toStatusDomain
import com.vrsalex.taskflow.domain.item.base.toStatusDto
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.time.Clock


// TO DTO

fun ItemCreate.toDto() = ItemCreateRequest(
    clientId = this.id,
    name = this.name,
    description = this.description,
    status = this.status.toStatusDto(),
    priority = this.priority,
    areaId = this.areaId,
    tags = this.tagIds,
)

fun ItemUpdate.toDto(): ItemUpdateRequest? = ItemUpdateRequest(
    clientId = this.id,
    id = this.serverId ?: return null,
    version = this.version,
    name = this.name.toOptionalDto(),
    description = this.description.toOptionalDto(),
    status = this.status.map { it.toStatusDto() }.toOptionalDto(),
    priority = this.priority.toOptionalDto(),
    areaId = this.areaId.toOptionalDto(),
    tags = this.tagIds.toOptionalDto(),
)

fun Item.toUpdateDto() = ItemUpdateRequest(
    clientId = this.id,
    id = this.serverId?: -1L,
    version = this.version,
    name = OptionalFieldDto.Defined(this.name),
    description = OptionalFieldDto.Defined(this.description),
    status = OptionalFieldDto.Defined(this.status.toStatusDto()),
    priority = OptionalFieldDto.Defined(this.priority),
    areaId = OptionalFieldDto.Defined(this.area?.id),
    tags = OptionalFieldDto.Defined(this.tags.map { it.id })
)

fun Item.toCreateDto() = ItemCreateRequest(
    clientId = this.id,
    name = this.name,
    description = this.description,
    status = this.status.toStatusDto(),
    priority = this.priority,
    areaId = this.area?.id,
    tags = this.tags.map { it.id }
)



// TO DOMAIN

fun ItemEntity.toDomain() = Item(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    isDeleted = this.isDeleted,
    name = this.name,
    description = this.description,
    status = this.status,
    type = this.type,
    priority = this.priority,

    tags = emptyList(),
    area = null
)

fun ItemRelation.toDomain() = Item(
    id = item.id,
    serverId = item.serverId,
    updatedAt = item.updatedAt,
    version = item.version,
    createdAt = item.createdAt,
    isSynced = item.isSynced,
    isDeleted = item.isDeleted,
    name = item.name,
    description = item.description,
    status = item.status,
    type = item.type,
    priority = item.priority,
    tags = tags.map { it.toDomain() },
    area = area?.toDomain()
)


// TO ENTITY

private fun ItemDto.toEntity() = ItemEntity(
    id = this.clientId,
    serverId = this.id,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = true,
    isDeleted = false,
    name = this.name,
    description = this.description,
    status = this.status.toStatusDomain(),
    type = this.type.toDomain(),
    priority = this.priority,
    areaId = this.areaId
)

fun ItemDto.toEntityWithRelations() = ItemWithRelations(
    entity = toEntity(),
    tags = tags
)

fun ItemCreate.toEntityWithRelations() = ItemWithRelations(
    entity = ItemEntity(
        id = id,
        serverId = null,
        updatedAt = Clock.System.now(),
        version = 0,
        createdAt = Clock.System.now(),
        isSynced = false,
        isDeleted = false,
        name = name,
        description = description,
        status = ItemStatus.ACTIVE,
        type = this.type,
        priority = priority,
        areaId = areaId
    ),
    tags = tagIds
)
