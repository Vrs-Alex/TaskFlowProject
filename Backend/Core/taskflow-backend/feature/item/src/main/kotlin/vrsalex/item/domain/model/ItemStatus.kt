package vrsalex.item.domain.model

import vrsalex.shared.api.item.base.ItemStatusDto

enum class ItemStatus {
    ACTIVE, ARCHIVED
}

fun ItemStatusDto.toItemStatus(): ItemStatus = when (this) {
    ItemStatusDto.ACTIVE -> ItemStatus.ACTIVE
    ItemStatusDto.ARCHIVED -> ItemStatus.ARCHIVED
}

fun ItemStatus.toItemStatusDto(): ItemStatusDto = when (this) {
    ItemStatus.ACTIVE -> ItemStatusDto.ACTIVE
    ItemStatus.ARCHIVED -> ItemStatusDto.ARCHIVED
}