package vrsalex.core.event_bus

import vrsalex.core.model.EntityType
import kotlin.time.Instant

sealed interface EventBusData {

    data class EntityChanged(
        val userId: Long,
        val entityId: Long,
        val entityType: EntityType,
        val time: Instant,
        val userDeviceId: String
    ): EventBusData

    data class Logout(val userId: Long) : EventBusData
}