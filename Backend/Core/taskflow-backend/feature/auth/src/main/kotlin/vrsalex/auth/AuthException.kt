package vrsalex.auth

import vrsalex.core.exception.AppException
import vrsalex.shared.api.exception.ServerStatusCode

sealed class AuthException(status: ServerStatusCode, message: String) : AppException(status, message) {

    class UserAlreadyExists() : AuthException(status = ServerStatusCode.Conflict, message = "Пользователь с такой почтой или username уже существует")

    class UserNotFound() : AuthException(status = ServerStatusCode.NotFound, message = "Пользователь не найден")

    class InvalidCredentials() : AuthException(status = ServerStatusCode.BadRequest, message = "Неверные данные для входа")

    class InvalidPassword() : AuthException(status = ServerStatusCode.BadRequest, message = "Пароль не соответствует требованиям")

    class RefreshTokenExpired() : AuthException(status = ServerStatusCode.Unauthorized, message = "Токен авторизации истек")

    class InvalidRefreshToken() : AuthException(status = ServerStatusCode.Unauthorized, message = "Неверный токен авторизации")

}