package com.vrsalex.taskflow.domain.sync.repository

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlin.uuid.Uuid

interface OutboxEntityHandler {
    suspend fun create(id: Uuid): Resource<Unit>
    suspend fun update(id: Uuid): Resource<Unit>
    suspend fun delete(id: Uuid): Resource<Unit>
}