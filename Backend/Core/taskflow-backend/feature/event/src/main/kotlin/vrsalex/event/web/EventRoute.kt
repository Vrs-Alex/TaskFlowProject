package vrsalex.event.web

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import vrsalex.core.routing.AppRouter
import vrsalex.core.routing.protected
import vrsalex.core.sync.syncRoute
import vrsalex.event.domain.Event
import vrsalex.event.domain.EventCreate
import vrsalex.event.domain.EventService
import vrsalex.event.domain.EventUpdate
import vrsalex.shared.api.item.event.EventCreateRequest
import vrsalex.shared.api.item.event.EventDto
import vrsalex.shared.api.item.event.EventItemUpdateRequest

class EventRoute: AppRouter {


    override fun Route.registerRoutes() {
        val service by inject<EventService>()

        protected {
            syncRoute<Event, EventCreate, EventUpdate,
                    EventCreateRequest, EventItemUpdateRequest, EventDto>(
                path = "/events",
                service = service,
                toCreateDomain = { it.toDomain() },
                toUpdateDomain = { it.toDomain() },
                toResponseDto = { it.toDto() }
            )
        }

    }
}