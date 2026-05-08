package com.vrsalex.taskflow.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vrsalex.taskflow.data.local.db.entity.workspace.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.item.ItemEntity
import com.vrsalex.taskflow.data.local.db.entity.workspace.ItemTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.workspace.TagEntity
import com.vrsalex.taskflow.data.local.db.entity.item.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.item.TaskLogEntity

data class TaskRelation(
    @Embedded
    val item: ItemEntity,

    @Relation(parentColumn = "id", entityColumn = "itemId")
    val task: TaskEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val taskLogs: List<TaskLogEntity>,

    @Relation(parentColumn = "areaId", entityColumn = "id")
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
