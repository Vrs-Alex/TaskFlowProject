package vrsalex.tag.web

import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import vrsalex.core.exception.AppException
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.core.sync.syncRoute
import vrsalex.core.value_object.Color
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import vrsalex.tag.domain.Tag
import vrsalex.tag.domain.TagCreate
import vrsalex.tag.domain.TagFilter
import vrsalex.tag.domain.TagService
import vrsalex.tag.domain.TagUpdate
import kotlin.uuid.Uuid

class TagRoute: AppRouter {

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