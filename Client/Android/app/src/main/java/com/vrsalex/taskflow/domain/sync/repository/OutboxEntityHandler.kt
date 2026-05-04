package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import vrsalex.shared.api.common.SyncDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface OutboxEntityHandler {
    suspend fun create(id: Uuid): Resource<SyncModel>
    suspend fun update(id: Uuid): Resource<SyncModel>
    suspend fun delete(id: Uuid): Resource<Unit>
    suspend fun markAsSynced(id: Uuid, syncModel: SyncModel)
    suspend fun findExisting(itemId: Uuid): SyncModel?
}

fun SyncDto.toSyncModel(): SyncModel = object : SyncModel {
    override val id: Uuid  = clientId
    override val serverId: Long = this@toSyncModel.id
    override val updatedAt: Instant = this@toSyncModel.updatedAt
    override val version: Int = this@toSyncModel.version
    override val createdAt: Instant = this@toSyncModel.createdAt
    override val isSynced: Boolean = true
    override val isDeleted: Boolean = false
}