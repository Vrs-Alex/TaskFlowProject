package vrsalex.core.database.utils

import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

suspend fun <T : Table> T.findOne(
    db: R2dbcDatabase,
    where: () -> Op<Boolean>
): ResultRow? = suspendTransaction(db) {
    selectAll().where(where).singleOrNull()
}


suspend fun <T : Table> T.exists(
    db: R2dbcDatabase,
    where: () -> Op<Boolean>
): Boolean = suspendTransaction(db) {
    selectAll().where(where).limit(1).any { true }
}