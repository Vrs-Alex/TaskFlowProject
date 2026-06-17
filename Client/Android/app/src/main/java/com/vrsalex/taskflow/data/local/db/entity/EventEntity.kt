package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid


@Entity(
    tableName = "event",
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class EventEntity(
    @PrimaryKey val id: Uuid,
    val startDate: Instant,
    val endDate: Instant?,
    val isAllDay: Boolean,
    val location: String?,
)
