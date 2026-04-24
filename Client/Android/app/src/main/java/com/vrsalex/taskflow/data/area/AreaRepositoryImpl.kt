package com.vrsalex.taskflow.data.area

import com.vrsalex.network.public.api.AreaApi
import com.vrsalex.taskflow.data.local.db.dao.AreaDao
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.area.Area
import com.vrsalex.taskflow.domain.area.AreaRepository
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant

class AreaRepositoryImpl(
    private val areaApi: AreaApi,
    private val areaDao: AreaDao,
    private val syncHandler: SyncHandler
) : AreaRepository {

    override fun get(): Flow<List<Area>> =
        areaDao.getAreas().map { list -> list.map { it.toDomain() } }


    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.AREA,
            lastSync = lastSync,
            fetch = areaApi::get,
            insert = { areaDao.insert(it.toEntity()) },
            delete = areaDao::delete
        )

}