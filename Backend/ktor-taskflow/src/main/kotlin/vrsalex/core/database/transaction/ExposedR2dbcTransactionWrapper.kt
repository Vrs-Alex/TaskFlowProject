package vrsalex.core.database.transaction

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.transactions.inTopLevelSuspendTransaction
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class ExposedR2dbcTransactionWrapper(
    private val db: R2dbcDatabase
) : TransactionWrapper {
    override suspend fun <T> dbTransaction(block: suspend () -> T): T {
        return suspendTransaction(db) { block() }
    }
}