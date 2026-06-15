package com.vrsalex.taskflow.data.workspace.area

import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.common.validation.worksapce.AreaName
import com.vrsalex.taskflow.domain.workspace.area.Area
import com.vrsalex.taskflow.domain.workspace.area.AreaCreate

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
