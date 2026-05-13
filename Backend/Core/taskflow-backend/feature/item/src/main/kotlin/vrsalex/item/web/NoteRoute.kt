package vrsalex.item.web

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.item.domain.model.Item
import vrsalex.item.domain.model.ItemCreate
import vrsalex.item.domain.model.ItemType
import vrsalex.item.domain.model.ItemUpdate
import vrsalex.item.domain.service.NoteService
import vrsalex.shared.api.item.base.ItemCreateRequest
import vrsalex.shared.api.item.base.ItemDto
import vrsalex.shared.api.item.base.ItemUpdateRequest
import kotlin.getValue

class NoteRoute: AppRouter {


    override fun Route.registerRoutes() {
        val service by inject<NoteService>()

        protected {
            syncRoute<Item, ItemCreate, ItemUpdate,
                    ItemCreateRequest, ItemUpdateRequest, ItemDto>(
                path = "/notes",
                service = service,
                toCreateDomain = { it.toDomain(ItemType.NOTE) },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )
        }
    }


}