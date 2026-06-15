package com.vrsalex.taskflow.domain.notify

import vrsalex.shared.api.notify.PushPlatformDto


data class DeviceInfo(
    val platform: PushPlatform,
    val token: String,
    val deviceId: String,
    val deviceName: String?,
    val isNotify: Boolean
)

enum class PushPlatform { FCM, APNS, WEB_PUSH, WNS }

fun PushPlatformDto.toDomain(): PushPlatform = when(this){
    PushPlatformDto.FCM -> PushPlatform.FCM
    PushPlatformDto.APNS -> PushPlatform.APNS
    PushPlatformDto.WEB_PUSH -> PushPlatform.WEB_PUSH
    PushPlatformDto.WNS -> PushPlatform.WNS
}

fun PushPlatform.toDto(): PushPlatformDto = when(this){
    PushPlatform.FCM -> PushPlatformDto.FCM
    PushPlatform.APNS -> PushPlatformDto.APNS
    PushPlatform.WEB_PUSH -> PushPlatformDto.WEB_PUSH
    PushPlatform.WNS -> PushPlatformDto.WNS
}
