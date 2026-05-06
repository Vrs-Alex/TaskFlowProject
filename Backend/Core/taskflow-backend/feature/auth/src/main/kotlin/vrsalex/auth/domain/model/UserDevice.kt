package vrsalex.auth.domain.model

data class UserDevice(
    val userId: Long,
    val fcmToken: String,
    val deviceName: String
)
