package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.database.core.manual.ItemStatusTable
import vrsalex.database.core.manual.ItemTypeTable

object ItemTable : LongIdTable("item", "item_id") {
    val owner = reference("owner_id", AppUserTable, onDelete = ReferenceOption.CASCADE)
    val type = reference("type_id", ItemTypeTable)
    val project = reference("project_id", ProjectTable, onDelete = ReferenceOption.SET_NULL)
    val area = reference("area_id", AreaTable, onDelete = ReferenceOption.SET_NULL)
    val status = reference("status_id", ItemStatusTable)
    val recurrence = reference("recurrence_id", RecurrenceRuleTable, onDelete = ReferenceOption.SET_NULL)
    val parent = reference("parent_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", length = 255)
    val description = text("description").nullable()
    val priority = short("priority").default(0)
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}