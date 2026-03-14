package vrsalex.database.core.manual

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object ItemStatusTable : IntIdTable("item_status") {
    val code = varchar("code", length = 50)
}