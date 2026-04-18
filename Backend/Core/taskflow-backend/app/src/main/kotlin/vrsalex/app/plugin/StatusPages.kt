package vrsalex.app.plugin

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import vrsalex.core.exception.AppException
import vrsalex.shared.api.exception.ServerErrorResponse
import vrsalex.shared.api.exception.ServerStatusCode
import java.io.IOException

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
                message = ServerErrorResponse(
                    status = ServerStatusCode.BadRequest,
                    message = "Не удалось обработать входящий запрос. Проверьте корректность данных"
                )
            )
        }

        exception<JsonConvertException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerErrorResponse(
                    status = ServerStatusCode.BadRequest,
                    message = "Не удалось обработать входящий запрос. Проверьте корректность данных"
                )
            )
        }


        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerErrorResponse(
                    status = ServerStatusCode.BadRequest,
                    message = cause.message ?: "Неверный аргумент"
                )
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerErrorResponse(
                    status = ServerStatusCode.BadRequest,
                    message = "Не удалось обработать входящий запрос. Проверьте корректность данных}"
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