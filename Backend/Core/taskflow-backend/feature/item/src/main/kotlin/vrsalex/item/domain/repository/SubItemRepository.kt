package vrsalex.item.domain.repository

import vrsalex.core.sync.model.SyncClientId
import vrsalex.core.sync.model.SyncModel
import vrsalex.core.sync.model.SyncUpdateModel
import vrsalex.core.sync.repository.SyncRepository

interface SubItemRepository<T : SyncModel, TCreate : SyncClientId, TUpdate : SyncUpdateModel>
    : SyncRepository<T, TCreate, TUpdate>