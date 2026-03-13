package vrsalex.feature.workspace.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.exception.AppException
import vrsalex.core.sync.BaseSyncService
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import vrsalex.feature.workspace.domain.repository.AreaRepository

class AreaService(
    repository: AreaRepository,
    private val transactionManager: TransactionManager
) : BaseSyncService<Area, AreaCreate, AreaUpdate, AreaRepository>(repository, transactionManager){

    override suspend fun create(data: AreaCreate, ownerId: Long): Long = transactionManager.dbTransaction {
        if (repository.existByOwnerIdAndName(ownerId, data.name))
            throw AppException.Conflict("Область задач с таким именем уже существует")

        super.create(data, ownerId)
    }

}