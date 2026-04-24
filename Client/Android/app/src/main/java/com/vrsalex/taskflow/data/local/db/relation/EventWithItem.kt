package com.vrsalex.taskflow.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.TagEntity

data class EventWithItemTagsAndArea(
    @Embedded
    val item: ItemEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val event: EventEntity,

    @Relation(
        parentColumn = "areaId",
        entityColumn = "id"
    )
    val area: AreaEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ItemTagCrossRef::class,
            parentColumn = "itemId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
)