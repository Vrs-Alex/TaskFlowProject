package com.vrsalex.taskflow.data.workspace.tag

import com.vrsalex.network.public.api.TagApi
import com.vrsalex.taskflow.data.local.db.dao.TagDao
import com.vrsalex.taskflow.data.sync.SyncHandler
import com.vrsalex.taskflow.domain.sync.models.SyncDbEntity
import com.vrsalex.taskflow.domain.workscape.tag.Tag
import com.vrsalex.taskflow.domain.workscape.tag.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant

class TagRepositoryImpl(
    private val tagApi: TagApi,
    private val tagDao: TagDao,
    private val syncHandler: SyncHandler
) : TagRepository {

    override fun get(): Flow<List<Tag>> =
        tagDao.getTags().map { list -> list.map { it.toDomain() } }


    override suspend fun sync(lastSync: Instant?) =
        syncHandler.sync(
            syncEntity = SyncDbEntity.TAG,
            lastSync = lastSync,
            fetch = tagApi::get,
            insert = { tagDao.upsert(it.toEntity()) },
            delete = tagDao::delete
        )

}