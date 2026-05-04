package com.vrsalex.taskflow.domain.workscape.tag

import com.vrsalex.taskflow.domain.common.model.OptionalField
import com.vrsalex.taskflow.domain.sync.models.SyncId
import com.vrsalex.taskflow.domain.sync.models.SyncModel
import com.vrsalex.taskflow.domain.sync.models.SyncUpdateModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Tag(
    override val id: Uuid,
    override val serverId: Long?,
    override val updatedAt: Instant,
    override val version: Int,
    override val createdAt: Instant,
    override val isSynced: Boolean,
    override val isDeleted: Boolean,

    val name: String,
    val color: String

): SyncModel


data class TagCreate(
    override val id: Uuid = Uuid.random(),
    val name: String,
    val color: String = "#71797E"
): SyncId


data class TagUpdate(
    override val id: Uuid,
    override val serverId: Long?,
    override val version: Int,
    val name: OptionalField<String> = OptionalField.Undefined,
    val color: OptionalField<String> = OptionalField.Undefined
): SyncUpdateModel