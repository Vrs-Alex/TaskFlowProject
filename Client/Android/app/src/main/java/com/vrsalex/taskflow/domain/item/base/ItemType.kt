package com.vrsalex.taskflow.domain.item.base

import vrsalex.shared.api.item.base.ItemTypeDto

enum class ItemType {
    EVENT
}

fun ItemType.toStatusDto(): ItemTypeDto {
    return when(this) {
        ItemType.EVENT -> ItemTypeDto.EVENT
    }
}

fun ItemTypeDto.toStatusDomain(): ItemType {
    return when(this) {
        ItemTypeDto.EVENT -> ItemType.EVENT
    }
}