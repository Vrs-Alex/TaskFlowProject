package com.vrsalex.taskflow.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity("area")
data class AreaEntity(
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