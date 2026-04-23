package vrsalex.item.domain.repository

import vrsalex.core.sync.repository.SyncRepository
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemUpdate
import kotlin.uuid.Uuid

interface ItemRepository: SyncRepository<Item, ItemCreate, ItemUpdate>{

    suspend fun updateTags(id: Long, tags: List<Uuid>)

    suspend fun deleteTags(id: Long, tags: List<Uuid>)

}