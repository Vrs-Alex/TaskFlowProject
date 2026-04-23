package vrsalex.area.web

import vrsalex.area.domain.Area
import vrsalex.area.domain.AreaCreate
import vrsalex.area.domain.AreaUpdate
import vrsalex.core.model.toOptional
import vrsalex.core.value_object.Color
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest

fun Area.toDto() = AreaDto(
    id = id,
    clientId = clientId,
    version = version,
    updatedAt = updatedAt,
    createdAt = createdAt,
    name = name,
    color = color.value
)

fun AreaCreateRequest.toDomain() = AreaCreate(
    clientId = this.clientId,
    name = this.name,
    color = Color(this.color)
)

fun AreaUpdateRequest.toDomain() = AreaUpdate(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    name = this.name.toOptional(),
    color = this.color.toOptional().map { Color(it) }
)