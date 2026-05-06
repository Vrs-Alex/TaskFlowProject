package vrsalex.core.database

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object TaskTable : LongIdTable("task", "id") {
    init {
        id.references(ItemTable.id, onDelete = ReferenceOption.CASCADE)
    }
    val dueDate = date("due_date")
    val dueTime = time("due_time").nullable()
    val recurrenceType = varchar("recurrence_type", length = 20).nullable()
    val recurrenceInterval = short("recurrence_interval").default(1)
    val recurrenceDays = short("recurrence_days").nullable()
    val recurrenceEndDate = date("recurrence_end_date").nullable()
    val recurrenceCount = integer("recurrence_count").nullable()
}