package vrsalex.area.web

import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import vrsalex.area.domain.*
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.core.sync.syncRoute
import vrsalex.core.value_object.Color
import vrsalex.shared.api.area.AreaCreateRequest
import vrsalex.shared.api.area.AreaDto
import vrsalex.shared.api.area.AreaUpdateRequest

class AreaRoute: AppRoute {

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