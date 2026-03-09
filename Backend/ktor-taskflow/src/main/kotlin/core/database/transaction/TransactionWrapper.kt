package vrsalex.core.database.transaction

interface TransactionWrapper {
    suspend fun <T> dbTransaction(block: suspend () -> T): T
}