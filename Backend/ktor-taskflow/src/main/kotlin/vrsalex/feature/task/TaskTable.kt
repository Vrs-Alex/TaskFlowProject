package vrsalex.feature.task

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.core.database.entity.ItemTable

object TaskTable : LongIdTable("task", "item_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val dueDate = date("due_date").nullable()
    val completedAt = timestamp("completed_at").defaultExpression(CurrentTimestamp).nullable()
}