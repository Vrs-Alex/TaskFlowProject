package vrsalex.feature.auth.exception

import error.ServerStatusCode
import io.ktor.http.HttpStatusCode
import vrsalex.core.exception.AppException

sealed class AuthException(status: ServerStatusCode, message: String) : AppException(status, message) {

    class UserAlreadyExists() : AuthException(status = ServerStatusCode.Conflict, message = "Пользователь с такой почтой или username уже существует")

    class UserNotFound() : AuthException(status = ServerStatusCode.NotFound, message = "Пользователь не найден")

    class InvalidPassword() : AuthException(status = ServerStatusCode.BadRequest, message = "Некорректный пароль")

    class RefreshTokenExpired() : AuthException(status = ServerStatusCode.BadRequest, message = "Refresh token истек")

    class InvalidRefreshToken() : AuthException(status = ServerStatusCode.BadRequest, message = "Некорректный refresh token")

}