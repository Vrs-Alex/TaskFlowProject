package com.vrsalex.taskflow.data.local.db.entity.workspace

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import kotlin.uuid.Uuid

@Entity(
    tableName = "item_tag",
    primaryKeys = ["itemId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("itemId"),
        Index("tagId")
    ]
)
data class ItemTagCrossRef(
    val itemId: Uuid,
    val tagId: Uuid
)