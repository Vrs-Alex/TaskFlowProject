package vrsalex.item.web

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import vrsalex.core.exception.AppException
import vrsalex.core.routing.AppRoute
import vrsalex.core.routing.protected
import vrsalex.core.security.user.UserPrincipal
import vrsalex.item.domain.conversion.ItemConversionService
import vrsalex.shared.api.item.conversion.ConvertItemRequest
import kotlin.uuid.Uuid

class ConversionRoute : AppRoute {

    override fun Route.registerRoutes() {
        val service by inject<ItemConversionService>()

        protected {
            post("/items/{clientId}/convert") {
                val principal = call.principal<UserPrincipal>()!!
                val clientId = Uuid.parseOrNull(call.parameters["clientId"] ?: "")
                    ?: throw AppException.BadRequest("Неверный ID")
                val request = call.receive<ConvertItemRequest>()
                val deviceId = call.request.headers["X-Device-Id"] ?: ""

                val result = service.convert(clientId, request, principal.internalId, deviceId)
                call.respond(HttpStatusCode.OK, result.toDto())
            }
        }
    }
}
