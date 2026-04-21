package vrsalex.item.domain.model

import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Item(
    override val userId: Long,
    override val id: Long,
    override val clientId: Uuid,
    override val updatedAt: Instant,
    override val version: Int,
    override val isDeleted: Boolean,
    override val createdAt: Instant,

    val name: String,
    val description: String?,
    val status: ItemStatus,
    val type: ItemType,
    val priority: Short,
    val areaId: Long?
): SyncModel


data class ItemCreate(
    override val clientId: Uuid,
    val name: String,
    val description: String?,
    val type: ItemType,
    val priority: Short,
    val areaId: Long?
): SyncClientId


data class ItemUpdate(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,

    val name: OptionalField<String> = OptionalField.Undefined,
    val description: OptionalField<String?> = OptionalField.Undefined,
    val status: OptionalField<ItemStatus> = OptionalField.Undefined,
    val priority: OptionalField<Short> = OptionalField.Undefined,
    val areaId: OptionalField<Long?> = OptionalField.Undefined,
): SyncUpdateModel