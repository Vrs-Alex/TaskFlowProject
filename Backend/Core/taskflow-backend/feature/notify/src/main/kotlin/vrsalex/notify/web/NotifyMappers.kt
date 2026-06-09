package vrsalex.notify.web

import vrsalex.notify.domain.model.UserDevice
import vrsalex.notify.domain.model.toDomain
import vrsalex.shared.api.notify.RegisterDeviceRequest

fun RegisterDeviceRequest.toUserDevice() = UserDevice(
    userId = 0,
    platform = platform.toDomain(),
    token = token,
    deviceId = deviceId,
    deviceName = deviceId,
    isPushEnabled = isPushEnabled
)