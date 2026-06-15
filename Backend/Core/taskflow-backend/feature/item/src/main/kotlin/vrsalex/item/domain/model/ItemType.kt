package vrsalex.item.domain.model

import vrsalex.core.model.EntityType
import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    NOTE, EVENT, TASK
}

fun ItemType.toEntityType(): EntityType = EntityType.valueOf(name)

fun ItemType.toItemTypeDto(): ItemTypeDto = when (this) {
    ItemType.NOTE -> ItemTypeDto.NOTE
    ItemType.EVENT -> ItemTypeDto.EVENT
    ItemType.TASK -> ItemTypeDto.TASK
}

fun ItemTypeDto.toItemType(): ItemType = when (this) {
    ItemTypeDto.NOTE -> ItemType.NOTE
    ItemTypeDto.EVENT -> ItemType.EVENT
    ItemTypeDto.TASK -> ItemType.TASK
}