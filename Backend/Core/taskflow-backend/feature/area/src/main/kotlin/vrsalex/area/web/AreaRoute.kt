package vrsalex.area.web

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.area.domain.Area
import vrsalex.area.domain.AreaCreate
import vrsalex.area.domain.AreaService
import vrsalex.area.domain.AreaUpdate
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest

class AreaRoute: AppRouter {

    override fun Route.registerRoutes() {
        val service by inject<AreaService>()

        protected {
            syncRoute<Area, AreaCreate, AreaUpdate, AreaCreateRequest, AreaUpdateRequest, AreaDto>(
                path = "/areas",
                service = service,
                toCreateDomain = { it.toDomain()},
                toUpdateDomain = { it.toDomain()},
                toResponseDto = { it.toDto()},
            )
        }
    }

}