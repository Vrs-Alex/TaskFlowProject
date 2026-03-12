package vrsalex.feature.workspace.web

import dto.workspace.area.AreaCreateRequest
import dto.workspace.area.AreaUpdateRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.map
import org.koin.ktor.ext.inject
import vrsalex.core.exception.AppException
import vrsalex.core.security.UserPrincipal
import vrsalex.core.sync.syncRoute
import vrsalex.feature.workspace.domain.model.Area
import vrsalex.feature.workspace.domain.model.AreaCreate
import vrsalex.feature.workspace.domain.model.AreaUpdate
import vrsalex.feature.workspace.domain.service.AreaService
import kotlin.time.Instant


fun Route.areaRoute() {
    val areaService by inject<AreaService>()

    authenticate("auth-jwt") {

        syncRoute<Area, AreaCreateRequest, AreaUpdateRequest, AreaCreate, AreaUpdate>(
            path = "/area",
            service = areaService,
            toCreateDomain = { it.toDomain() },
            toUpdateDomain = { it.toDomain() },
            toResponseDto = { it.toDto() }
        )

    }
}