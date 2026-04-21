package vrsalex.core.event_bus

import kotlinx.datetime.LocalDate
import vrsalex.shared.api.realtime.EntityType
import kotlin.time.Instant
import kotlin.uuid.Uuid

sealed interface EventBusData {

    data class EntityChanged(
        val userId: Long,
        val entityId: Long,
        val entityType: EntityType,
        val time: Instant
    ): EventBusData

    data class Logout(val userId: Long) : EventBusData
}