package com.vrsalex.taskflow.data.local.db.entity.sync

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity("sync")
data class SyncEntity(
    @PrimaryKey
    val entity: String,
    val lastSyncedAt: Instant
)
