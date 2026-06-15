package vrsalex.core.exception

import vrsalex.shared.api.exception.ErrorCode
import vrsalex.shared.api.exception.ServerStatusCode


open class AppException(
    val status: ServerStatusCode,
    override val message: String,
    val code: ErrorCode? = null
) : RuntimeException(message) {

    class BadRequest(message: String, code: ErrorCode? = null) : AppException(ServerStatusCode.BadRequest, message, code)

    class Conflict(message: String, code: ErrorCode? = null) : AppException(ServerStatusCode.Conflict, message, code)

    class NotFound(message: String = "Ресурс не найден", code: ErrorCode? = null) : AppException(ServerStatusCode.NotFound, message, code)

    class InvalidFormat(field: String = "Неверные данные") : AppException(ServerStatusCode.BadRequest, field)

    class Unauthorized(message: String = "Неавторизованный доступ") : AppException(ServerStatusCode.Unauthorized, message)

    class Gone(message: String) : AppException(ServerStatusCode.Gone, message)

    class InternalServerError(message: String) : AppException(ServerStatusCode.InternalServerError, message)

}
