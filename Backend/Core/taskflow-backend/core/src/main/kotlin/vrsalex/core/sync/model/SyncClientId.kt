package vrsalex.core.sync.model

import kotlin.uuid.Uuid

interface SyncClientId {
    val clientId: Uuid
}