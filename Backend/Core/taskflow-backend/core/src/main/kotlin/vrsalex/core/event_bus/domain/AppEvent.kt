package vrsalex.core.event_bus.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vrsalex.core.model.EntityType
import vrsalex.shared.api.realtime.EntityTypeDto
import kotlin.time.Instant

@Serializable
sealed interface AppEvent {

    @Serializable
    @SerialName("entity_changed")
    data class EntityChanged(
        val userId: Long,
        val entityId: Long,
        val entityType: EntityType,
        val time: Instant,
        val sourceDeviceId: String
    ) : AppEvent

    @Serializable
    @SerialName("push_notification")
    data class PushNotification(
        val userId: Long,
        val sourceDeviceId: String,
        val title: String,
        val description: String
    ) : AppEvent

    @Serializable
    @SerialName("logout")
    data class Logout(val userId: Long) : AppEvent
}