@file:OptIn(ExperimentalSerializationApi::class)

package vrsalex.shared.api.realtime

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.time.Instant


@Serializable
@JsonClassDiscriminator("event_type")
sealed interface RealtimeEventDto {

    @Serializable @SerialName("entity_changed")
    data class EntityChanged(
        val entityId: Long,
        val entityType: EntityTypeDto,
        val time: Instant
    ) : RealtimeEventDto


    @Serializable @SerialName("logout")
    object Logout : RealtimeEventDto

    @Serializable @SerialName("notification")
    data class Notification(
        val title: String,
        val body: String,
        val isSilent: Boolean = false
    ) : RealtimeEventDto
}