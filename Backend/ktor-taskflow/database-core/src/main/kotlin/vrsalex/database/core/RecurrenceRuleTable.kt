package vrsalex.database.core

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.timestamp
import vrsalex.database.core.manual.RecurrenceEndTypeTable
import vrsalex.database.core.manual.RecurrenceFrequencyTable

object RecurrenceRuleTable : LongIdTable("recurrence_rule", "rule_id") {
    val frequency = reference("frequency_id", RecurrenceFrequencyTable)
    val intervalValue = integer("interval_value").default(1)
    val daysOfWeek = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val endType = reference("end_type_id", RecurrenceEndTypeTable)
    val endCount = integer("end_count").nullable()
    val endDate = date("end_date").nullable()
    val clientId = uuid("client_id")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val isDeleted = bool("is_deleted").default(false)
    val version = integer("version").default(1)
}