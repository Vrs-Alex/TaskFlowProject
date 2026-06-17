package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid


@Entity(
    tableName = "task_log",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("taskId")]
)
data class TaskLogEntity(
    @PrimaryKey val id: Uuid,
    val taskId: Uuid,
    val date: LocalDate,
    val completedAt: Instant?,
    @Embedded override val sync: SyncColumns,
): ISyncDbColumns
