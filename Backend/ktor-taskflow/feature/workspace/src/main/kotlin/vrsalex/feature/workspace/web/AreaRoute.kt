package vrsalex.feature.workspace.web

import vrsalex.api.dto.workspace.area.AreaCreateRequest
import vrsalex.api.dto.workspace.area.AreaUpdateRequest
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.FeatureRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import vrsalex.feature.workspace.domain.service.AreaService


class AreaRoute(): FeatureRouter {

    override fun Route.registerRoutes() {
        val areaService by inject<AreaService>()

        protected {
            syncRoute<Area, AreaCreateRequest, AreaUpdateRequest, AreaCreate, AreaUpdate>(
                path = "/area",
                service = areaService,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
            ) { it.toDto() }

        }
    }
}