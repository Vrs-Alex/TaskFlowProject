package vrsalex.database.core

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object EventTable : LongIdTable("event", "item_id") {
    val item = reference("item_id", ItemTable, onDelete = ReferenceOption.CASCADE)
    val startTime = timestamp("start_time").defaultExpression(CurrentTimestamp)
    val endTime = timestamp("end_time").defaultExpression(CurrentTimestamp)
    val isAllDay = bool("is_all_day").default(false)
    val location = varchar("location", length = 255).nullable()
}