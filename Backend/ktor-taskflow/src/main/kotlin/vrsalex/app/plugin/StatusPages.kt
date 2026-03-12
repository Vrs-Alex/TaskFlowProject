package vrsalex.app.plugin

import error.ServerErrorResponse
import error.ServerStatusCode
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import vrsalex.core.exception.AppException

fun Application.configureStatusPages() {
    install(StatusPages) {


        exception<AppException> { call, cause ->
            call.respond(
                status = HttpStatusCode.fromValue(cause.status.value),
                message = ServerErrorResponse(
                    status = cause.status,
                    message = cause.message
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

        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerErrorResponse(
                    status = ServerStatusCode.BadRequest,
                    message = cause.message // TODO only dev
                )
            )
        }


        // TODO only for dev
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }

    }

}