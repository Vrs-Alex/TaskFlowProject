package vrsalex.feature.workspace.web

import dto.workspace.area.AreaCreateRequest
import dto.workspace.area.AreaDto
import dto.workspace.area.AreaUpdateRequest
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate

fun AreaCreateRequest.toDomain() = AreaCreate(
    name = this.name,
    color = this.color,
    clientId = this.clientId
)

fun AreaUpdateRequest.toDomain() = AreaUpdate(
    id = this.id,
    clientId = this.clientId,
    name = this.name,
    color = this.color,
    version = this.version
)


fun Area.toDto() = AreaDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    name = this.name,
    color = this.color,
    createdAt = this.createdAt
)