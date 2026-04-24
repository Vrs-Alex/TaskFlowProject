package com.vrsalex.taskflow.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity

data class EventWithItem(
    @Embedded
    val item: ItemEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val event: EventEntity
)