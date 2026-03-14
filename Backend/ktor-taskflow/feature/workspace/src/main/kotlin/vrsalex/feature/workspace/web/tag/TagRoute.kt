package vrsalex.feature.workspace.web.tag

import vrsalex.api.dto.workspace.tag.TagCreateRequest
import vrsalex.api.dto.workspace.tag.TagUpdateRequest
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.FeatureRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate
import vrsalex.feature.workspace.domain.service.TagService

class TagRoute: FeatureRouter{

    override fun Route.registerRoutes() {
        val tagService by inject<TagService>()

        protected {
            syncRoute<Tag, TagCreateRequest, TagUpdateRequest, TagCreate, TagUpdate>(
                path = "/tag",
                service = tagService,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
            ) { it.toDto() }
        }
    }

}