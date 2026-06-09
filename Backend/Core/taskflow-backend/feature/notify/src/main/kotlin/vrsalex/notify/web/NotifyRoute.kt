package vrsalex.notify.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.RouteProtection
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.shared.api.notify.RegisterDeviceRequest


class NotifyRoute(private val service: NotifyService) : AppRoute {

    override fun Route.registerRoutes() {

        protected(
            protection = RouteProtection.JWT
        ) {
            post("/notify/device") {
                val principal = call.principal<UserPrincipal>()!!
                val request = call.receive<RegisterDeviceRequest>()
                service.registerDevice(request.toUserDevice().copy(userId = principal.internalId))
                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
