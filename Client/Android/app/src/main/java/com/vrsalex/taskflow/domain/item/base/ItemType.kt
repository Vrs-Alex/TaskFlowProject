package com.vrsalex.taskflow.domain.item.base

import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    EVENT, TASK
}

fun ItemType.toDto(): ItemTypeDto {
    return when(this) {
        ItemType.EVENT -> ItemTypeDto.EVENT
        ItemType.TASK -> ItemTypeDto.TASK
    }
}

fun ItemTypeDto.toDomain(): ItemType {
    return when(this) {
        ItemTypeDto.EVENT -> ItemType.EVENT
        ItemTypeDto.TASK -> ItemType.TASK
    }
}