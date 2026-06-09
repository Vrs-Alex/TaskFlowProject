package vrsalex.notify.domain.model

internal data class PushInfo(
    val platform: PushPlatform,
    val token: String
)