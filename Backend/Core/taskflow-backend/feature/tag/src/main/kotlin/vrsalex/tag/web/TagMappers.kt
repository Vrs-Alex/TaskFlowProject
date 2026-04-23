package vrsalex.tag.web

import vrsalex.core.model.toOptional
import vrsalex.core.value_object.Color
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import vrsalex.tag.domain.Tag
import vrsalex.tag.domain.TagCreate
import vrsalex.tag.domain.TagUpdate

fun TagCreateRequest.toDomain() = TagCreate(
    clientId = this.clientId,
    name = this.name,
    color = Color(this.color),
)

fun TagUpdateRequest.toDomain() = TagUpdate(
    id = this.id,
    clientId = this.clientId,
    name = this.name.toOptional(),
    color = this.color.toOptional().map { Color(it) },
    version = this.version,
)

fun Tag.toDto() = TagDto(
    id = this.id,
    clientId = this.clientId,
    version = this.version,
    updatedAt = this.updatedAt,
    name = this.name,
    color = this.color.value,
    createdAt = this.createdAt
)