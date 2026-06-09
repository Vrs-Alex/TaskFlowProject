package vrsalex.tag.domain

import vrsalex.core.sync.repository.SyncRepository

interface TagRepository: SyncRepository<Tag, TagCreate, TagUpdate> {

    suspend fun existByUserIdAndName(userId: Long, name: String): Boolean

    suspend fun search(filter: TagFilter, userId: Long): List<Tag>

}