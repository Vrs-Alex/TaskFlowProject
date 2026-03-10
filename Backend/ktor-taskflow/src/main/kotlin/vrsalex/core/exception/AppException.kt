package vrsalex.core.exception

import error.ServerStatusCode

open class AppException(val status: ServerStatusCode, override val message: String) : RuntimeException(message) {

    class NotFoundException(message: String = "Ресурс не найден") : AppException(ServerStatusCode.NotFound, message)

    class UnauthorizedException(message: String = "Неавторизованный доступ") : AppException(ServerStatusCode.Unauthorized, message)

    class InvalidFormat(val field: String) : AppException(ServerStatusCode.BadRequest, "Неверный формат поля: $field")



}
