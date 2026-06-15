package com.vrsalex.taskflow.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vrsalex.taskflow.data.local.db.entity.AreaEntity
import com.vrsalex.taskflow.data.local.db.entity.EventEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteEntity
import com.vrsalex.taskflow.data.local.db.entity.NoteTagCrossRef
import com.vrsalex.taskflow.data.local.db.entity.TagEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskEntity
import com.vrsalex.taskflow.data.local.db.entity.TaskLogEntity


data class NoteRelation(
    @Embedded val note: NoteEntity,
    @Relation(parentColumn = "areaId", entityColumn = "id")
    val area: AreaEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class, parentColumn = "noteId", entityColumn = "tagId")
    )
    val tags: List<TagEntity>,
)

data class TaskRelation(
    @Embedded val note: NoteEntity,
    @Relation(parentColumn = "id", entityColumn = "id")
    val task: TaskEntity,
    @Relation(parentColumn = "areaId", entityColumn = "id")
    val area: AreaEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class, parentColumn = "noteId", entityColumn = "tagId")
    )
    val tags: List<TagEntity>,
    @Relation(parentColumn = "id", entityColumn = "taskId")
    val logs: List<TaskLogEntity>,
)

data class EventRelation(
    @Embedded val note: NoteEntity,
    @Relation(parentColumn = "id", entityColumn = "id")
    val event: EventEntity,
    @Relation(parentColumn = "areaId", entityColumn = "id")
    val area: AreaEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class, parentColumn = "noteId", entityColumn = "tagId")
    )
    val tags: List<TagEntity>,
)
