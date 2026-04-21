package vrsalex.item.data

import org.jetbrains.exposed.v1.core.ResultRow
import vrsalex.core.database.ItemTable
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemStatus
import vrsalex.item.domain.model.ItemType


fun ResultRow.toItem(): Item = Item(
    userId = this[ItemTable.userId].value,
    id = this[ItemTable.id].value,
    clientId = this[ItemTable.clientId],
    updatedAt = this[ItemTable.updatedAt],
    version = this[ItemTable.version],
    isDeleted = this[ItemTable.isDeleted],
    createdAt = this[ItemTable.createdAt],
    name = this[ItemTable.name],
    description = this[ItemTable.description],
    status = ItemStatus.valueOf(this[ItemTable.status]),
    type = ItemType.valueOf(this[ItemTable.type]),
    priority = this[ItemTable.priority],
    areaId = this[ItemTable.areaId]?.value
)