package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.domain.note.base.NotePriority
import com.vrsalex.taskflow.domain.note.base.NoteStatus
import com.vrsalex.taskflow.domain.note.base.NoteType
import kotlin.uuid.Uuid


@Entity(tableName = "note", indices = [Index("areaId")])
data class NoteEntity(
    @PrimaryKey val id: Uuid,
    val name: String,
    val description: String?,
    val type: NoteType,
    val status: NoteStatus,
    val priority: NotePriority,
    val areaId: Uuid?,
    @Embedded override val sync: SyncColumns,
): ISyncDbColumns