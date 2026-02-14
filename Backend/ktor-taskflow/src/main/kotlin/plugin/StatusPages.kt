package vrsalex.plugin

import dto.ErrorResponse
import error.ServerErrorCode
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {

        // TODO only for dev
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ErrorResponse(
                    status = ServerErrorCode.BAD_REQUEST.status,
                    code = ServerErrorCode.BAD_REQUEST,
                    message = "Bad Request"
                )
            )
        }

        exception<ContentTransformationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf(
                    "error" to "Invalid request body",
                    "details" to "Could not parse JSON to expected format: ${cause.message?.take(100)}..."
                )
            )
        }

        exception<JsonConvertException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf(
                    "error" to "Invalid JSON format",
                    "details" to cause.message?.take(150)
                )
            )
        }
    }

}