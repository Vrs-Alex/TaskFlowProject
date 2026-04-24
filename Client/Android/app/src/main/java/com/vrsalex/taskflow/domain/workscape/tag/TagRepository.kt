package com.vrsalex.taskflow.domain.workscape.tag

import com.vrsalex.taskflow.data.sync.SyncableRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface TagRepository : SyncableRepository {

    fun get(): Flow<List<Tag>>

}