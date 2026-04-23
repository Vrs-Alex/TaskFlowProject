package vrsalex.core.database

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object TagTable : LongIdTable("tag", "id"), SyncTable {
    override val userId = reference("user_id", UserTable, onDelete = ReferenceOption.CASCADE)
    override val clientId = uuid("client_id")
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)
    override val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)

    val name = varchar("name", length = 100)
    val color = varchar("color", length = 7)
}