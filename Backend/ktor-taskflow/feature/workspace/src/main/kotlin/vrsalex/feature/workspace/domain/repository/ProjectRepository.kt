package vrsalex.feature.workspace.domain.repository

import vrsalex.core.sync.SyncRepository
import vrsalex.feature.workspace.domain.model.Project
import vrsalex.feature.workspace.domain.model.ProjectCreate
import vrsalex.feature.workspace.domain.model.ProjectUpdate

interface ProjectRepository: SyncRepository<Project, ProjectCreate, ProjectUpdate>{

    suspend fun existByOwnerIdAndName(ownerId: Long, name: String): Boolean

}