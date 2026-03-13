package vrsalex.feature.reminder

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.ItemTable

object ReminderTable : LongIdTable("reminder", "reminder_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val remindAt = timestamp("remind_at").defaultExpression(CurrentTimestamp)
    val isSent = bool("is_sent").default(false)
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}