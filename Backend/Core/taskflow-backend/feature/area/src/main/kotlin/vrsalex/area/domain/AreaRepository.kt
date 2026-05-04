package vrsalex.area.domain

import kotlinx.coroutines.flow.Flow
import vrsalex.core.sync.repository.SyncRepository

interface AreaRepository: SyncRepository<Area, AreaCreate, AreaUpdate>{

    suspend fun search(filter: AreaFilter, userId: Long): List<Area>

}