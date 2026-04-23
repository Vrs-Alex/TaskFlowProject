package vrsalex.core.exception

import vrsalex.shared.api.exception.ServerStatusCode


open class AppException(val status: ServerStatusCode, override val message: String) : RuntimeException(message) {

    class BadRequest(override val message: String): AppException(ServerStatusCode.BadRequest, message)

    class InvalidFormat(field: String = "Неверные данные") : AppException(ServerStatusCode.BadRequest, field)

    class Unauthorized(message: String = "Неавторизованный доступ") : AppException(ServerStatusCode.Unauthorized, message)

    class NotFound(message: String = "Ресурс не найден") : AppException(ServerStatusCode.NotFound, message)

    class Conflict(message: String) : AppException(ServerStatusCode.Conflict, message)

    class Gone(message: String) : AppException(ServerStatusCode.Gone, message)

    class InternalServerError(message: String) : AppException(ServerStatusCode.InternalServerError, message)


}
