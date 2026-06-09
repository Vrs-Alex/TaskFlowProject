package vrsalex.shared.api.notify

import kotlinx.serialization.Serializable

@Serializable
enum class PushPlatformDto { FCM, APNS, WEB_PUSH, WNS }