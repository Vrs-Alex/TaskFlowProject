package com.vrsalex.taskflow.domain.sync.models

import kotlin.uuid.Uuid

interface SyncId {
    val id: Uuid
}