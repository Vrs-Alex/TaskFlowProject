package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import kotlin.uuid.Uuid

@Entity(
    tableName = "note_tag",
    primaryKeys = ["noteId", "tagId"],
    indices = [Index("tagId")]
)
data class NoteTagCrossRef(
    val noteId: Uuid,
    val tagId: Uuid,
)
