package vrsalex.core.database.entity

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.manual.ProjectStatusTable

object ProjectTable : LongIdTable("project", "project_id") {
    val owner = reference("owner_id", AppUserTable, onDelete = ReferenceOption.CASCADE)
    val area = reference("area_id", AreaTable, onDelete = ReferenceOption.SET_NULL)
    val status = reference("status_id", ProjectStatusTable)
    val name = varchar("name", length = 255)
    val description = text("description").nullable()
    val color = varchar("color", length = 7).nullable()
    val dueDate = date("due_date").nullable()
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}