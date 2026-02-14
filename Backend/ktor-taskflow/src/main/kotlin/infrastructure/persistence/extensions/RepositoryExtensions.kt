package vrsalex.infrastructure.persistence.extensions

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

suspend fun <ID : Comparable<ID>, T> IdTable<ID>.singleBy(
    db: R2dbcDatabase,
    column: Column<ID>,
    value: ID,
    mapper: (ResultRow) -> T
): T? = suspendTransaction(db) {
    this@singleBy.selectAll()
        .where { column eq value }
        .map(mapper)
        .singleOrNull()
}