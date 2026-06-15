package vrsalex.notify.web

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
