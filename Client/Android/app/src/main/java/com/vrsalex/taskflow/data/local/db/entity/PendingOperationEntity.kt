package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.domain.sync.models.PendingOperation
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity("pending_operation")
data class PendingOperationEntity(
    @PrimaryKey
    val itemId: Uuid,
    val entityType: SyncDbEntity,
    val operation: PendingOperation,
    val createdAt: Instant = Clock.System.now()
)