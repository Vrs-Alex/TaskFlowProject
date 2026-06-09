package vrsalex.notify.domain.model

data class UserDevice(
    val userId: Long,
    val platform: PushPlatform,
    val token: String,
    val deviceId: String,
    val deviceName: String,
    val isPushEnabled: Boolean
)