package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object AreaTable : LongIdTable("area", "area_id"), SyncTable {
    override val ownerId = reference("owner_id", AppUserTable)
    override val clientId = uuid("client_id").uniqueIndex()
    override val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    override val version = integer("version").default(1)
    override val isDeleted = bool("is_deleted").default(false)

    val name = varchar("name", 100)
    val color = varchar("color", 7).default("#FFFFFF")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}