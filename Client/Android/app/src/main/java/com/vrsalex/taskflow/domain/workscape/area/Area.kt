package com.vrsalex.taskflow.domain.workscape.area

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.sync.models.SyncId
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.models.SyncUpdateModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Area(
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,

    val name: String,
    val color: String

): SyncModel


data class AreaCreate(
    override val id: Uuid,
    val name: String,
    val color: String
): SyncId


data class AreaUpdate(
    override val id: Uuid,
    override val serverId: Long,
    override val version: Int,
    val name: OptionalField<String> = OptionalField.Undefined,
    val color: OptionalField<String> = OptionalField.Undefined
): SyncUpdateModel