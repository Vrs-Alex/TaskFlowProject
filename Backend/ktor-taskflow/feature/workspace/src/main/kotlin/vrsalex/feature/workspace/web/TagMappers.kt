package vrsalex.feature.workspace.web

import vrsalex.api.dto.workspace.tag.TagCreateRequest
import vrsalex.api.dto.workspace.tag.TagDto
import vrsalex.api.dto.workspace.tag.TagUpdateRequest
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate

fun TagCreateRequest.toDomain() = TagCreate(
    clientId = this.clientId,
    name = this.name,
    color = this.color,
)

fun TagUpdateRequest.toDomain() = TagUpdate(
    id = this.id,
    clientId = this.clientId,
    name = this.name,
    color = this.color,
    version = this.version,
)

fun Tag.toDto() = TagDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    name = this.name,
    color = this.color
)