package vrsalex.api.dto.common

import kotlin.uuid.Uuid

interface SyncUpdateEntity {
    val id: Long
    val clientId: Uuid
    val version: Int
}