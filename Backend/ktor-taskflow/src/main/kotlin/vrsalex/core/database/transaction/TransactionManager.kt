package vrsalex.core.database.transaction

interface TransactionManager {

    suspend fun <T> dbTransaction(readOnly: Boolean = false, block: suspend () -> T): T

}