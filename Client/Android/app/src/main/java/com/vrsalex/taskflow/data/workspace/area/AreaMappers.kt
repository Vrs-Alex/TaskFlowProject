package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.taskflow.data.local.db.entity.workspace.AreaEntity
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import com.vrsalex.taskflow.domain.workscape.area.Area
import com.vrsalex.taskflow.domain.workscape.area.AreaCreate
import com.vrsalex.taskflow.domain.workscape.area.AreaUpdate
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest
import vrsalex.shared.api.common.OptionalFieldDto
import kotlin.time.Clock

// TO DTO


fun AreaCreate.toDto() = AreaCreateRequest(
    clientId = this.id,
    name = this.name,
    color = this.color
)

fun AreaUpdate.toDto() = AreaUpdateRequest(
    clientId = this.id,
    id = this.serverId ?: 0L,
    version = this.version,
    name = this.name.toOptionalDto(),
    color = this.color.toOptionalDto()
)

fun Area.toCreateDto() = AreaCreateRequest(
    clientId = id,
    name = name,
    color = color
)

fun Area.toUpdateDto() = AreaUpdateRequest(
    clientId = id,
    id = serverId!!,
    version = version,
    name = OptionalFieldDto.Defined(name),
    color = OptionalFieldDto.Defined(color)
)


// TO DOMAIN

fun AreaEntity.toDomain(): Area = Area(
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

fun AreaDto.toEntity(): AreaEntity = AreaEntity(
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


fun AreaCreate.toEntity() = AreaEntity(
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


