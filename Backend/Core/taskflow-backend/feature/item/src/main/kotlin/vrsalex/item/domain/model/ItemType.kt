package vrsalex.item.domain.model

import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    EVENT, TASK
}

fun ItemType.toItemTypeDto(): ItemTypeDto = when (this) {
    ItemType.EVENT -> ItemTypeDto.EVENT
    ItemType.TASK -> ItemTypeDto.TASK
}

fun ItemTypeDto.toItemType(): ItemType = when (this) {
    ItemTypeDto.EVENT -> ItemType.EVENT
    ItemTypeDto.TASK -> ItemType.TASK
}