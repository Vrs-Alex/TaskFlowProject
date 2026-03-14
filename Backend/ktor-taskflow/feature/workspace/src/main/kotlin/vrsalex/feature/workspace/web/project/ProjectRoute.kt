package vrsalex.feature.workspace.web.project

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.api.dto.workspace.project.ProjectCreateRequest
import vrsalex.api.dto.workspace.project.ProjectUpdateRequest
import vrsalex.core.routing.FeatureRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.feature.workspace.domain.model.Project
import vrsalex.feature.workspace.domain.model.ProjectCreate
import vrsalex.feature.workspace.domain.model.ProjectUpdate
import vrsalex.feature.workspace.domain.service.ProjectService

class ProjectRoute: FeatureRouter {

    override fun Route.registerRoutes() {
        val projectService by inject<ProjectService>()

        protected {
            syncRoute<Project, ProjectCreateRequest, ProjectUpdateRequest, ProjectCreate, ProjectUpdate>(
                path = "/project",
                service = projectService,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )
        }
    }
}