package vrsalex.database.core.manual

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object RecurrenceEndTypeTable : IntIdTable("recurrence_end_type") {
    val code = varchar("code", length = 50)
}