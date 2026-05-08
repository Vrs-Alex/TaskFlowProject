package com.vrsalex.taskflow.data.local.db.entity.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vrsalex.taskflow.data.local.db.entity.sync.SyncDbModel
import com.vrsalex.taskflow.domain.item.base.ItemStatus
import com.vrsalex.taskflow.domain.item.base.ItemType
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity("item")
data class ItemEntity(
    @PrimaryKey
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,
    override val isDeleted: Boolean,

    val name: String,
    val description: String?,
    val status: ItemStatus,
    val type: ItemType,
    val priority: Short,
    val areaId: Uuid?
): SyncDbModel
