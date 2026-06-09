package vrsalex.shared.api.notify

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val deviceId: String,
    val platform: PushPlatformDto,
    val token: String,
    val deviceName: String,
    val isPushEnabled: Boolean
)


