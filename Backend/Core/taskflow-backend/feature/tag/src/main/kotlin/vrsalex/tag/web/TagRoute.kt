package vrsalex.tag.web

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.shared.api.tag.TagCreateRequest
import vrsalex.shared.api.tag.TagDto
import vrsalex.shared.api.tag.TagUpdateRequest
import vrsalex.tag.domain.Tag
import vrsalex.tag.domain.TagCreate
import vrsalex.tag.domain.TagService
import vrsalex.tag.domain.TagUpdate

class TagRoute: AppRouter {

    override fun Route.registerRoutes() {
        val service by inject<TagService>()

        protected {
            syncRoute<Tag, TagCreate, TagUpdate, TagCreateRequest, TagUpdateRequest, TagDto>(
                path = "/tags",
                service = service,
                toCreateDomain = { it.toDomain()},
                toUpdateDomain = { it.toDomain()},
                toResponseDto = { it.toDto() }
            )
        }
    }

}