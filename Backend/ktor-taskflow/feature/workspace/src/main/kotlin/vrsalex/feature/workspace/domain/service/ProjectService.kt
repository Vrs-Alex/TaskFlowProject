package vrsalex.feature.workspace.domain.service

import vrsalex.core.database.transaction.TransactionManager
import vrsalex.core.exception.AppException
import vrsalex.core.sync.BaseSyncService
import vrsalex.feature.workspace.domain.model.Project
import vrsalex.feature.workspace.domain.model.ProjectCreate
import vrsalex.feature.workspace.domain.model.ProjectUpdate
import vrsalex.feature.workspace.domain.repository.ProjectRepository

class ProjectService(
    repository: ProjectRepository,
    private val transactionManager: TransactionManager,
): BaseSyncService<Project, ProjectCreate, ProjectUpdate, ProjectRepository>(repository, transactionManager) {


    override suspend fun create(data: ProjectCreate, ownerId: Long): Long = transactionManager.dbTransaction {
        if (repository.existByOwnerIdAndName(ownerId, data.name)) {
            throw AppException.Conflict("Проект с таким названием уже существует")
        }
        super.create(data, ownerId)
    }

}