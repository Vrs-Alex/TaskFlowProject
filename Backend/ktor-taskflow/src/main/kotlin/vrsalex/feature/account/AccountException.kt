package vrsalex.feature.account

import error.ServerStatusCode
import vrsalex.core.exception.AppException

sealed class AccountException(status: ServerStatusCode, message: String) : AppException(status, message) {

    class UserAlreadyExists() : AccountException(status = ServerStatusCode.Conflict, message = "Пользователь с такой почтой или username уже существует")

    class UserNotFound() : AccountException(status = ServerStatusCode.NotFound, message = "Пользователь не найден")

    class InvalidCredentials() : AccountException(status = ServerStatusCode.BadRequest, message = "Неверные данные для входа")

    class InvalidPassword() : AccountException(status = ServerStatusCode.BadRequest, message = "Пароль не соответствует требованиям")

    class RefreshTokenExpired() : AccountException(status = ServerStatusCode.BadRequest, message = "Токен авторизации истек")

    class InvalidRefreshToken() : AccountException(status = ServerStatusCode.BadRequest, message = "Неверный токен авторизации")

}