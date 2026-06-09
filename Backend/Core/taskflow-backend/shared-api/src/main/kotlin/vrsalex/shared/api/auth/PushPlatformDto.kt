package vrsalex.shared.api.auth

import kotlinx.serialization.Serializable

@Serializable
enum class PushPlatformDto { FCM, APNS, WEB_PUSH, WNS }