package com.vrsalex.taskflow.domain.workscape.area

import com.vrsalex.taskflow.data.sync.SyncableRepository
import kotlinx.coroutines.flow.Flow

interface AreaRepository: SyncableRepository {

    fun get(): Flow<List<Area>>

}