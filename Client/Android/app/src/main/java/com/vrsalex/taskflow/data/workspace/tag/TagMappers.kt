package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.taskflow.data.local.db.entity.workspace.TagEntity
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.taskflow.domain.workscape.tag.TagCreate
import com.vrsalex.taskflow.domain.workscape.tag.TagUpdate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import kotlin.time.Clock

// TO DTO

fun TagUpdate.toDto() = TagUpdateRequest(
    clientId = id,
    id = serverId ?: 0L,
    version = version,
    name = name.toOptionalDto(),
    color = color.toOptionalDto()
)

fun TagCreate.toEntity() = TagEntity(
    id = id,
    serverId = null,
    updatedAt = Clock.System.now(),
    version = 0,
    createdAt = Clock.System.now(),
    isSynced = false,
    isDeleted = false,
    name = name,
    color = color
)

fun Tag.toCreateDto() = TagCreateRequest(
    clientId = id,
    name = name,
    color = color
)

fun Tag.toUpdateDto() = TagUpdateRequest(
    clientId = id,
    id = serverId!!,
    version = version,
    name = OptionalFieldDto.Defined(name),
    color = OptionalFieldDto.Defined(color)
)


// TO DOMAIN

fun TagEntity.toDomain() = Tag(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    isDeleted = this.isDeleted,
    name = this.name,
    color = this.color
)

// TO ENTITY

fun TagDto.toEntity() = TagEntity(
    id = this.clientId,
    serverId = this.id,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = true,
    isDeleted = false,
    name = this.name,
    color = this.color
)
