package vrsalex.core.database

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object EventTable : LongIdTable("event", "id") {
    init {
        id.references(ItemTable.id, onDelete = ReferenceOption.CASCADE)
    }
    val startDate = timestamp("start_date").defaultExpression(CurrentTimestamp)
    val endDate = timestamp("end_date").nullable()
    val isAllDay = bool("is_all_day")
    val location = text("location").nullable()
}