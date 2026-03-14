package vrsalex.feature.workspace.web.area

import vrsalex.api.dto.workspace.area.AreaCreateRequest
import vrsalex.api.dto.workspace.area.AreaDto
import vrsalex.api.dto.workspace.area.AreaUpdateRequest
import vrsalex.core.model.toModelOptional
import vrsalex.core.value_object.Color
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate

fun AreaCreateRequest.toDomain() = AreaCreate(
    name = this.name,
    color = Color(this.color),
    clientId = this.clientId
)

fun AreaUpdateRequest.toDomain() = AreaUpdate(
    id = this.id,
    clientId = this.clientId,
    name = this.name.toModelOptional(),
    color = this.color.toModelOptional().map { Color(it) },
    version = this.version
)


fun Area.toDto() = AreaDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    name = this.name,
    color = this.color.value,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt
)