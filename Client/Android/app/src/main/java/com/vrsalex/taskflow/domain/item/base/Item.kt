package com.vrsalex.taskflow.domain.item.base

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.sync.models.SyncId
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.models.SyncUpdateModel
import com.vrsalex.taskflow.domain.tag.Tag
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Item(
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,

    val name: String,
    val description: String?,
    val status: ItemStatus,
    val type: ItemType,
    val priority: Short,
    val areaId: Long?,
    val tags: List<Tag>
): SyncModel


data class ItemCreate(
    override val id: Uuid,
    val name: String,
    val description: String?,
    val type: ItemType,
    val priority: Short,
    val areaId: Long?,
    val tagIds: List<Uuid>
): SyncId


data class ItemUpdate(
    override val id: Uuid,
    override val serverId: Long,
    override val version: Int,

    val name: OptionalField<String> = OptionalField.Undefined,
    val description: OptionalField<String?> = OptionalField.Undefined,
    val status: OptionalField<ItemStatus> = OptionalField.Undefined,
    val priority: OptionalField<Short> = OptionalField.Undefined,
    val areaId: OptionalField<Long?> = OptionalField.Undefined,
    val tagIds: OptionalField<List<Uuid>> = OptionalField.Undefined,
): SyncUpdateModel