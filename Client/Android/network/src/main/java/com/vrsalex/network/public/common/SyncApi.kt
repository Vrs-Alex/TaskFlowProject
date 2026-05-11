package com.vrsalex.network.public.common

import vrsalex.shared.api.common.ModelDto
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface SyncApi<T, TCreate, TUpdate> {

    suspend fun getById(id: Uuid): NetworkResult<T?>

    suspend fun sync(lastSync: Instant? = null): NetworkResult<List<ModelDto<T>>>

    suspend fun syncItem(id: Uuid): NetworkResult<ModelDto<T>>

    suspend fun create(data: TCreate): NetworkResult<T>

    suspend fun update(data: TUpdate): NetworkResult<T>

    suspend fun delete(id: Uuid, serverId: Long, version: Int): NetworkResult<Unit>

}