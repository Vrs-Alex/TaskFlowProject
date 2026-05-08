package com.vrsalex.taskflow.data.local.db.relation

import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import kotlin.uuid.Uuid

data class ItemWithRelations(
    val entity: ItemEntity,
    val tags: List<Uuid>
)