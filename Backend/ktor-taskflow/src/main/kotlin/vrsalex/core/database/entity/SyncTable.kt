package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import kotlin.time.Instant
import kotlin.uuid.Uuid


interface SyncTable {
    val id: Column<EntityID<Long>>
    val ownerId: Column<EntityID<Long>>
    val clientId: Column<Uuid>
    val version: Column<Int>
    val updatedAt: Column<Instant>
    val isDeleted: Column<Boolean>
}