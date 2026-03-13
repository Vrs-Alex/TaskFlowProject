package vrsalex.core.database.entity.manual

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object RecurrenceFrequencyTable : IntIdTable("recurrence_frequency") {
    val code = varchar("code", length = 50)
}