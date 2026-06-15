package vrsalex.item.domain.model

import vrsalex.core.exception.ensure
import vrsalex.core.model.OptionalField
import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.shared.api.exception.ErrorCode
import kotlin.text.isNotBlank
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
    val areaId: Uuid?,
    val tags: List<Uuid>
): SyncModel


data class ItemCreate(
    override val clientId: Uuid,
    val name: String,
    val description: String?,
    val type: ItemType,
    val status: ItemStatus,
    val priority: Short,
    val areaId: Uuid?,
    val tags: List<Uuid>
): SyncClientId {
    init {
        ensure(name.isNotBlank(), ErrorCode.ITEM_NAME_BLANK)
        ensure(name.length <= 255, ErrorCode.ITEM_NAME_TOO_LONG)
        description?.length?.let {
            ensure(it <= 5000, ErrorCode.ITEM_DESCRIPTION_TOO_LONG)
        }
    }
}

fun Item.toItemCreate(): ItemCreate = ItemCreate(
    clientId = clientId,
    name = name,
    description = description,
    type = type,
    status = status,
    priority = priority,
    areaId = areaId,
    tags = tags
)


data class ItemUpdate(
    override val clientId: Uuid,
    override val id: Long,
    override val version: Int,

    val name: OptionalField<String> = OptionalField.Undefined,
    val description: OptionalField<String?> = OptionalField.Undefined,
    val status: OptionalField<ItemStatus> = OptionalField.Undefined,
    val priority: OptionalField<Short> = OptionalField.Undefined,
    val areaId: OptionalField<Uuid?> = OptionalField.Undefined,
    val tags: OptionalField<List<Uuid>> = OptionalField.Undefined
): SyncUpdateModel {
    init {
        name.onDefined {
            ensure(it.isNotBlank(), ErrorCode.ITEM_NAME_BLANK)
        }
        name.onDefined {
            ensure(it.length <= 255, ErrorCode.ITEM_NAME_TOO_LONG)
        }
        description.onDefined { description ->
            description?.length?.let {
                ensure(it <= 5000, ErrorCode.ITEM_DESCRIPTION_TOO_LONG)
            }
        }
    }
}