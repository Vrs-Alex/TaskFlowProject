package vrsalex.feature.workspace.web

import dto.workspace.tag.TagCreateRequest
import dto.workspace.tag.TagUpdateRequest
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.sync.syncRoute
import vrsalex.feature.workspace.domain.model.Tag
import vrsalex.feature.workspace.domain.model.TagCreate
import vrsalex.feature.workspace.domain.model.TagUpdate
import vrsalex.feature.workspace.domain.service.TagService

fun Route.tagRoute() {

    val tagService by inject<TagService>()


    authenticate("auth-jwt") {

        syncRoute<Tag, TagCreateRequest, TagUpdateRequest, TagCreate, TagUpdate>(
            path = "/tag",
            service = tagService,
            toCreateDomain = { it.toDomain() },
            toUpdateDomain = { it.toDomain() },
            toResponseDto = { it.toDto() }
        )

    }

}