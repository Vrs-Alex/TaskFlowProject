package com.vrsalex.taskflow.domain.item.base

import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    NOTE, TASK, EVENT
}

fun ItemType.toDto(): ItemTypeDto {
    return when(this) {
        ItemType.NOTE -> ItemTypeDto.NOTE
        ItemType.TASK -> ItemTypeDto.TASK
        ItemType.EVENT -> ItemTypeDto.EVENT
    }
}

fun ItemTypeDto.toDomain(): ItemType {
    return when(this) {
        ItemTypeDto.NOTE -> ItemType.NOTE
        ItemTypeDto.TASK -> ItemType.TASK
        ItemTypeDto.EVENT -> ItemType.EVENT
    }
}