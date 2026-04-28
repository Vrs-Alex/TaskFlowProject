package com.vrsalex.taskflow.domain.sync.models

import kotlin.uuid.Uuid

interface SyncUpdateModel: SyncId {
    override val id: Uuid
    val serverId: Long?
    val version: Int
}