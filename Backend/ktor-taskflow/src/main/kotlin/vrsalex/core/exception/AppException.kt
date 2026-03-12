package vrsalex.core.exception

import error.ServerStatusCode

open class AppException(val status: ServerStatusCode, override val message: String) : RuntimeException(message) {

    class BadRequest(override val message: String): AppException(ServerStatusCode.BadRequest, message)

    class InvalidFormat(field: String) : AppException(ServerStatusCode.BadRequest, "Неверный формат поля: $field")

    class Unauthorized(message: String = "Неавторизованный доступ") : AppException(ServerStatusCode.Unauthorized, message)

    class NotFound(message: String = "Ресурс не найден") : AppException(ServerStatusCode.NotFound, message)

    class Conflict(message: String) : AppException(ServerStatusCode.Conflict, message)


}
