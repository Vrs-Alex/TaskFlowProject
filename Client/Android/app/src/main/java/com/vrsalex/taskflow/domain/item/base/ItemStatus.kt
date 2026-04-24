package com.vrsalex.taskflow.domain.item.base

import vrsalex.shared.api.item.base.ItemStatusDto

enum class ItemStatus {
    ACTIVE, ARCHIVED
}

fun ItemStatus.toStatusDto(): ItemStatusDto {
    return when(this) {
        ItemStatus.ACTIVE -> ItemStatusDto.ACTIVE
        ItemStatus.ARCHIVED -> ItemStatusDto.ARCHIVED
    }
}

fun ItemStatusDto.toStatusDomain(): ItemStatus {
    return when(this) {
        ItemStatusDto.ACTIVE -> ItemStatus.ACTIVE
        ItemStatusDto.ARCHIVED -> ItemStatus.ARCHIVED
    }
}