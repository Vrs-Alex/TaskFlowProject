package com.vrsalex.taskflow.data.note

import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteEntity
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.mapper.newLocalSync
import com.vrsalex.taskflow.data.local.db.mapper.toSyncColumns
import com.vrsalex.taskflow.data.local.db.mapper.toSyncModel
import com.vrsalex.taskflow.data.local.db.relation.NoteRelation
import com.vrsalex.taskflow.data.workspace.area.toDomain
import com.vrsalex.taskflow.data.workspace.tag.toDomain
import com.vrsalex.taskflow.domain.common.validation.note.NoteDescription
import com.vrsalex.taskflow.domain.common.validation.note.NoteName
import com.vrsalex.taskflow.domain.note.base.Note
import com.vrsalex.taskflow.domain.note.base.NoteCreate
import com.vrsalex.taskflow.domain.note.base.NotePriority
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteType
import com.vrsalex.taskflow.domain.note.base.NoteUpdate
import vrsalex.shared.api.common.OptionalFieldDto
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemStatusDto
import vrsalex.shared.api.item.base.ItemTypeDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.time.Clock
import kotlin.uuid.Uuid

fun NoteEntity.toNote(area: AreaEntity?, tags: List<TagEntity>): Note = Note(
    name = NoteName.trusted(name),
    description = description?.let { NoteDescription.trusted(it) },
    type = type,
    status = status,
    priority = priority,
    area = area?.toDomain(),
    tags = tags.map { it.toDomain() },
    syncModel = sync.toSyncModel(id),
)

fun NoteRelation.toDomain(): Note = note.toNote(area, tags)

fun NoteCreate.toEntity(): NoteEntity = NoteEntity(
    id = syncModelCreate.id,
    name = name.value,
    description = description?.value,
    type = type,
    status = status,
    priority = priority,
    areaId = area?.syncModel?.id,
    sync = newLocalSync(),
)

fun NoteCreate.tagIds(): List<Uuid> = tags.map { it.syncModel.id }

fun NoteUpdate.applyTo(current: NoteEntity): NoteEntity {
    var e = current
    name.onDefined { e = e.copy(name = it.value) }
    description.onDefined { e = e.copy(description = it?.value) }
    type.onDefined { e = e.copy(type = it) }
    status.onDefined { e = e.copy(status = it) }
    priority.onDefined { e = e.copy(priority = it) }
    area.onDefined { e = e.copy(areaId = it?.syncModel?.id) }
    return e.copy(sync = e.sync.copy(isSynced = false, updatedAt = Clock.System.now()))
}

fun NoteUpdate.tagIdsOrNull(): List<Uuid>? {
    var ids: List<Uuid>? = null
    tags.onDefined { list -> ids = list.map { it.syncModel.id } }
    return ids
}


fun ItemDto.toEntity(): NoteEntity = NoteEntity(
    id = clientId,
    name = name,
    description = description,
    type = type.toDomain(),
    status = status.toDomain(),
    priority = priority.toNotePriority(),
    areaId = areaId,
    sync = toSyncColumns(),
)

// --- Локальная dirty-запись → запросы на сервер (push). Отправляем полное состояние заметки. ---
// Базовые билдеры работают над NoteEntity + tagIds, чтобы их переиспользовали Task/Event (их base — та же заметка).

fun NoteStatus.toDto(): ItemStatusDto = when (this) {
    NoteStatus.ACTIVE -> ItemStatusDto.ACTIVE
    NoteStatus.ARCHIVE -> ItemStatusDto.ARCHIVED
}

fun NoteEntity.toItemCreateRequest(tagIds: List<Uuid>): ItemCreateRequest = ItemCreateRequest(
    clientId = id,
    name = name,
    description = description,
    status = status.toDto(),
    priority = priority.value,
    areaId = areaId,
    tags = tagIds,
)

fun NoteEntity.toItemUpdateRequest(tagIds: List<Uuid>): ItemUpdateRequest = ItemUpdateRequest(
    clientId = id,
    id = requireNotNull(sync.serverId) { "serverId обязателен для UPDATE" },
    version = sync.version,
    name = OptionalFieldDto.Defined(name),
    description = OptionalFieldDto.Defined(description),
    status = OptionalFieldDto.Defined(status.toDto()),
    priority = OptionalFieldDto.Defined(priority.value),
    areaId = OptionalFieldDto.Defined(areaId),
    tags = OptionalFieldDto.Defined(tagIds),
)

fun NoteRelation.toCreateRequest(): ItemCreateRequest = note.toItemCreateRequest(tags.map { it.id })
fun NoteRelation.toUpdateRequest(): ItemUpdateRequest = note.toItemUpdateRequest(tags.map { it.id })

fun ItemTypeDto.toDomain(): NoteType = when (this) {
    ItemTypeDto.NOTE -> NoteType.NOTE
    ItemTypeDto.TASK -> NoteType.TASK
    ItemTypeDto.EVENT -> NoteType.EVENT
}

fun ItemStatusDto.toDomain(): NoteStatus = when (this) {
    ItemStatusDto.ACTIVE -> NoteStatus.ACTIVE
    ItemStatusDto.ARCHIVED -> NoteStatus.ARCHIVE
}

fun Short.toNotePriority(): NotePriority {
    val raw = this
    return NotePriority.entries.firstOrNull { it.value == raw } ?: NotePriority.P0
}
