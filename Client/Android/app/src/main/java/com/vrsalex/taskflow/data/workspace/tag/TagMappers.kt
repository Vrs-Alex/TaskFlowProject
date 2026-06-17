package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncColumns
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import vrsalex.shared.api.tag.TagDto
import com.vrsalex.taskflow.domain.common.validation.Color
import com.vrsalex.taskflow.domain.common.validation.worksapce.TagName
import com.vrsalex.taskflow.domain.workspace.tag.Tag
import com.vrsalex.taskflow.domain.workspace.tag.TagCreate

fun TagEntity.toDomain(): Tag = Tag(
    name = TagName.trusted(name),
    color = Color(color),
    syncModel = sync.toSyncModel(id),
)

fun TagCreate.toEntity(): TagEntity = TagEntity(
    id = syncModelCreate.id,
    name = name.value,
    color = color.value,
    sync = newLocalSync(),
)

fun TagDto.toEntity(): TagEntity = TagEntity(
    id = clientId,
    name = name,
    color = color,
    sync = toSyncColumns(),
)
