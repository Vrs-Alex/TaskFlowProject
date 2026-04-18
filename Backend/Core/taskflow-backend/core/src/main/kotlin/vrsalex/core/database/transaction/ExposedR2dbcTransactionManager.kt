package vrsalex.core.database.transaction

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class ExposedR2dbcTransactionManager(
    private val db: R2dbcDatabase
) : TransactionManager {

    override suspend fun <T> dbTransaction(readOnly: Boolean, block: suspend () -> T): T {
        return suspendTransaction(db, readOnly = readOnly) { block() }
    }
}