package vrsalex.auth.domain.model.device

import vrsalex.shared.api.auth.PushPlatformDto

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