package vrsalex.area.web

import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import vrsalex.area.domain.Area
import vrsalex.area.domain.AreaCreate
import vrsalex.area.domain.AreaFilter
import vrsalex.area.domain.AreaService
import vrsalex.area.domain.AreaUpdate
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.core.sync.syncRoute
import vrsalex.core.value_object.Color
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest
import kotlin.text.toBoolean

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

            route("/areas") {
                get {
                    val principal = call.principal<UserPrincipal>()!!
                    val filter = AreaFilter(
                        name      = call.request.queryParameters["name"],
                        nameExact = call.request.queryParameters["nameExact"]?.toBoolean() ?: false,
                        color     = call.request.queryParameters["color"]?.let { Color(it) }
                    )
                    val tags = service.search(filter, principal.internalId)
                    call.respond(tags.map { it.toDto() })
                }
            }
        }
    }

}