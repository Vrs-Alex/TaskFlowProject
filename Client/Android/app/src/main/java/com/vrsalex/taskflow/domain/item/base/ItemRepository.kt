package com.vrsalex.taskflow.domain.item.base

import com.vrsalex.taskflow.domain.sync.repository.CrudRepository
import com.vrsalex.taskflow.domain.sync.repository.SyncableRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

interface ItemRepository<T, TCreate, TUpdate> : CrudRepository<T, TCreate, TUpdate> {

    fun getByDate(date: Instant): Flow<List<T>>

}