package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object TagTable : LongIdTable("tag", "tag_id"), SyncTable {
    override val ownerId = reference("owner_id", AppUserTable, onDelete = ReferenceOption.CASCADE)
    override val clientId = uuid("client_id")
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)

    val name = varchar("name", length = 50)
    val color = varchar("color", length = 7).default("'#808080'")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}