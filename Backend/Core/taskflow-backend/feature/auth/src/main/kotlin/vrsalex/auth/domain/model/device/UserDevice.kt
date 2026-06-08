package vrsalex.auth.domain.model.device

data class UserDevice(
    val userId: Long,
    val platform: PushPlatform,
    val token: String,
    val deviceId: String,
    val deviceName: String
)