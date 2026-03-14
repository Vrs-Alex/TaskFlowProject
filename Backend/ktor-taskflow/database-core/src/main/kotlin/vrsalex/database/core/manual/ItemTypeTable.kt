package vrsalex.database.core.manual

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object ItemTypeTable : IntIdTable("item_type") {
    val code = varchar("code", length = 50)
}