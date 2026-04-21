package vrsalex.item.domain.model

import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    EVENT
}

fun ItemType.toItemTypeDto(): ItemTypeDto = when (this) {
    ItemType.EVENT -> ItemTypeDto.EVENT
}

fun ItemTypeDto.toItemType(): ItemType = when (this) {
    ItemTypeDto.EVENT -> ItemType.EVENT
}