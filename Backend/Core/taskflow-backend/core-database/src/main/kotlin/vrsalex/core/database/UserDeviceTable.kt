package vrsalex.core.database

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object UserDeviceTable : IdTable<Long>("user_device") {
    override val id = long("id").autoIncrement().entityId()
    val userId = reference("user_id", UserTable)
    val platform = varchar("platform", 32)
    val token = text("token")
    val deviceId = varchar("device_id", 255)
    val deviceName = text("device_name").nullable()
    val pushEnabled = bool("push_enabled")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}