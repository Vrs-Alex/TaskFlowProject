package vrsalex.feature.workspace.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.sync.BaseSyncService
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import vrsalex.feature.workspace.domain.repository.AreaRepository

class AreaService(
    repository: AreaRepository,
    transactionManager: TransactionManager
) : BaseSyncService<Area, AreaCreate, AreaUpdate>(repository, transactionManager)