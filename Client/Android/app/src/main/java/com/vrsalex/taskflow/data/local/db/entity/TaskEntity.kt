package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.domain.note.task.RecurrenceType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.Uuid

/**
 * Extension-строка задачи. PK = note.id. Своего sync-конверта НЕТ — версию/синк несёт note.
 */
@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class TaskEntity(
    @PrimaryKey val id: Uuid,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val recurrenceType: RecurrenceType?,
    val recurrenceDays: Short?,
    val recurrenceEndDate: LocalDate?,
    val recurrenceCount: Int?,
    val recurrenceInterval: Short,
)
