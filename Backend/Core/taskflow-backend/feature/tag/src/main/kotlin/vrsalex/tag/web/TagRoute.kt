package vrsalex.tag.web

import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.core.sync.syncRoute
import vrsalex.core.value_object.Color
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import vrsalex.tag.domain.*

class TagRoute: AppRoute {

    override fun Route.registerRoutes() {
        val service by inject<TagService>()

        protected {
            syncRoute<Tag, TagCreate, TagUpdate, TagCreateRequest, TagUpdateRequest, TagDto>(
                path = "/tags",
                service = service,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )

            route("/tags") {
                get {
                    val principal = call.principal<UserPrincipal>()!!
                    val filter = TagFilter(
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