package com.vrsalex.taskflow.domain.workscape.tag

import com.vrsalex.taskflow.domain.sync.repository.CrudRepository
import com.vrsalex.taskflow.domain.sync.repository.SyncableRepository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface TagRepository : CrudRepository<Tag, TagCreate, TagUpdate>