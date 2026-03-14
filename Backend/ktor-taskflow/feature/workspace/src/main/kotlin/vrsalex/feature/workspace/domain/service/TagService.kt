package vrsalex.feature.workspace.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.exception.AppException
import vrsalex.core.sync.BaseSyncService
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate
import vrsalex.feature.workspace.domain.repository.TagRepository

class TagService(
    repository: TagRepository,
    private val transactionManager: TransactionManager
): BaseSyncService<Tag, TagCreate, TagUpdate, TagRepository>(repository, transactionManager){

    override suspend fun create(data: TagCreate, ownerId: Long): Long = transactionManager.dbTransaction {
        if (repository.existByOwnerIdAndName(ownerId, data.name))
            throw AppException.Conflict("Тег с таким названием уже существует")

        super.create(data, ownerId)
    }

}