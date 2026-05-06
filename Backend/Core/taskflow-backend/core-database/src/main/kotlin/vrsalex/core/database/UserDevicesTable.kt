package vrsalex.core.database

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.Instant

object UserDevicesTable : IdTable<Long>("user_device") {
    override val id = long("id").autoIncrement().entityId()
    val userId = reference("user_id", UserTable)
    val fcmToken = text("fcm_token")
    val deviceName = text("device_name").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}