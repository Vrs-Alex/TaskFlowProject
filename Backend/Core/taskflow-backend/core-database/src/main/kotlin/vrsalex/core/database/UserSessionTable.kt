package vrsalex.core.database

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.uuid.Uuid

object UserSessionTable : IdTable<Uuid>("user_session") {
    override val id = uuid("id").entityId()
    val userId = reference("user_id", UserTable)
    val tokenHash = text("token_hash")
    val agent = text("agent")
    val ipAddress = varchar("ip", length = 50)
    val expiresAt = timestamp("expires_at").defaultExpression(CurrentTimestamp)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}