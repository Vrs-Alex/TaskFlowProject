package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.domain.sync.model.SyncEntity
import kotlin.time.Instant


@Entity(tableName = "sync_cursor")
data class SyncCursorEntity(
    @PrimaryKey val entity: SyncEntity,
    val lastSync: Instant,
)
