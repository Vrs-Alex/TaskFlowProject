package com.vrsalex.taskflow.data.local.db.entity.item

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.data.local.db.entity.sync.SyncDbModel
import kotlinx.datetime.LocalDate
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "task_log"
)
data class TaskLogEntity(
    @PrimaryKey
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,
    override val isDeleted: Boolean,

    val taskId: Uuid,
    val date: LocalDate
): SyncDbModel
