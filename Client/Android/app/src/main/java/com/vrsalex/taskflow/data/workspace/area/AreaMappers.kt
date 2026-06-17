package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.SyncColumns
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.common.validation.worksapce.AreaName
import com.vrsalex.taskflow.domain.workspace.area.Area
import com.vrsalex.taskflow.domain.workspace.area.AreaCreate
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest
import vrsalex.shared.api.common.OptionalFieldDto

fun AreaDto.toEntity(): AreaEntity = AreaEntity(
    id = clientId,
    name = name,
    color = color,
    sync = SyncColumns(
        serverId = id,
        version = version,
        updatedAt = updatedAt,
        createdAt = createdAt,
        isDeleted = false,
        isSynced = true,
    ),
)

fun AreaEntity.toDomain(): Area = Area(
    name = AreaName.trusted(name),
    color = Color(color),
    syncModel = sync.toSyncModel(id),
)

fun AreaCreate.toEntity(): AreaEntity = AreaEntity(
    id = syncModelCreate.id,
    name = name.value,
    color = color.value,
    sync = newLocalSync(),
)

// --- Локальная dirty-запись → запросы на сервер (push) ---

fun AreaEntity.toCreateRequest(): AreaCreateRequest = AreaCreateRequest(
    clientId = id,
    name = name,
    color = color,
)

fun AreaEntity.toUpdateRequest(): AreaUpdateRequest = AreaUpdateRequest(
    id = requireNotNull(sync.serverId) { "serverId обязателен для UPDATE" },
    clientId = id,
    version = sync.version,
    name = OptionalFieldDto.Defined(name),
    color = OptionalFieldDto.Defined(color),
)

