package com.vrsalex.taskflow.data.local.db.entity.item

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.domain.item.task.RecurrenceType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.uuid.Uuid

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey
    val itemId: Uuid,
    val dueDate: LocalDate,
    val dueTime: LocalTime?,
    val recurrenceType: RecurrenceType?,
    val recurrenceInterval: Int = 1,
    val recurrenceDays: Short?,
    val recurrenceEndDate: LocalDate?,
    val recurrenceCount: Int?
)
