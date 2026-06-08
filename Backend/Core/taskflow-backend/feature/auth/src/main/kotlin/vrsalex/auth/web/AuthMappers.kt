package vrsalex.auth.web

import vrsalex.auth.domain.model.UserCreate
import vrsalex.auth.domain.model.UserLogin
import vrsalex.auth.domain.model.device.UserDevice
import vrsalex.auth.domain.model.device.toDomain
import vrsalex.core.value_object.Email
import vrsalex.core.value_object.UserPassword
import vrsalex.core.value_object.Username
import vrsalex.shared.api.auth.LoginRequest
import vrsalex.shared.api.auth.RegisterDeviceRequest
import vrsalex.shared.api.auth.RegisterRequest

fun LoginRequest.toUser(): UserLogin = UserLogin(
    identity = identity,
    password = password,
    ipAddress = "",
    agent = ""
)

fun RegisterRequest.toUserCreate() = UserCreate(
    username = Username(this.username),
    email = Email(this.email),
    fullName = this.fullName,
    password = UserPassword(this.password)
)

fun RegisterDeviceRequest.toUserDevice() = UserDevice(
    userId = 0,
    platform = platform.toDomain(),
    token = token,
    deviceId = deviceId,
    deviceName = deviceId
)