package vrsalex.shared.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val deviceId: String,
    val platform: PushPlatformDto,
    val token: String,
    val deviceName: String
)

@Serializable
enum class PushPlatformDto { FCM, APNS, WEB_PUSH, WNS }
