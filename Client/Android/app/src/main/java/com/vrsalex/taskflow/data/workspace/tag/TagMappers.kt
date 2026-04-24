package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import vrsalex.shared.api.tag.TagDto

fun TagEntity.toDomain() = Tag(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    name = this.name,
    color = this.color
)

fun TagDto.toEntity() = TagEntity(
    id = this.clientId,
    serverId = this.id,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = true,
    name = this.name,
    color = this.color
)