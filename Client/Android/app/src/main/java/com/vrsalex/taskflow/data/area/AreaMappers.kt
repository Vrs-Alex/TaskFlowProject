package com.vrsalex.taskflow.data.area

import android.R.attr.description
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.domain.area.Area
import com.vrsalex.taskflow.domain.area.AreaCreate
import com.vrsalex.taskflow.domain.area.AreaUpdate
import com.vrsalex.taskflow.domain.common.model.toOptionalDto
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest

fun AreaEntity.toDomain(): Area = Area(
    id = this.id,
    serverId = this.serverId,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = this.isSynced,
    name = this.name,
    color = this.color
)

fun AreaDto.toEntity(): AreaEntity = AreaEntity(
    id = this.clientId,
    serverId = this.id,
    updatedAt = this.updatedAt,
    version = this.version,
    createdAt = this.createdAt,
    isSynced = true,
    name = this.name,
    color = this.color
)

fun AreaCreate.toDto() = AreaCreateRequest(
    clientId = this.id,
    name = this.name,
    color = this.color
)

fun AreaUpdate.toDto() = AreaUpdateRequest(
    clientId = this.id,
    id = this.serverId,
    version = this.version,
    name = this.name.toOptionalDto(),
    color = this.color.toOptionalDto()
)
