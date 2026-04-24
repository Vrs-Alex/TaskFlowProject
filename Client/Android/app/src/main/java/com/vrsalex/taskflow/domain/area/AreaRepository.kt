package com.vrsalex.taskflow.domain.area

import com.vrsalex.taskflow.data.sync.SyncableRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface AreaRepository: SyncableRepository {

    fun get(): Flow<List<Area>>

}