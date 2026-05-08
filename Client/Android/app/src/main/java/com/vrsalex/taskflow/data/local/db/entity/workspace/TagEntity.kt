package com.vrsalex.taskflow.data.local.db.entity.workspace

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.data.local.db.entity.sync.SyncDbModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity("tag")
data class TagEntity(
    @PrimaryKey
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,
    override val isDeleted: Boolean,

    val name: String,
    val color: String
): SyncDbModel