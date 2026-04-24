package com.vrsalex.taskflow.domain.tag

import com.vrsalex.taskflow.domain.common.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TagRepository {

    fun getTags(): Flow<List<Tag>>

    suspend fun syncTags(lastSync: Instant? = null): Resource<Unit>

}