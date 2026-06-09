package vrsalex.shared.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val deviceId: String,
    val platform: PushPlatformDto,
    val token: String,
    val deviceName: String,
    val isPushEnabled: Boolean
)


