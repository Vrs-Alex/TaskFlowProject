package com.vrsalex.taskflow.domain.item.event

import com.vrsalex.taskflow.data.sync.SyncableRepository
import com.vrsalex.taskflow.domain.common.model.Resource
import com.vrsalex.taskflow.domain.sync.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface EventRepository: SyncableRepository {

    fun get(): Flow<List<Event>>

    fun getByDate(date: Instant): Flow<List<Event>>

}