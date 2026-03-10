package vrsalex.app.di

import org.koin.dsl.module
import vrsalex.core.database.connection.FlywayMigration
import vrsalex.core.database.connection.R2dbcFactory
import vrsalex.core.database.transaction.ExposedR2dbcTransactionWrapper
import vrsalex.core.database.transaction.TransactionWrapper

val databaseModule = module {

    single(createdAtStart = true) {
        FlywayMigration(get()).apply {
            migrate()
        }
    }

    single { R2dbcFactory.createDatabase(get()) }

    single<TransactionWrapper> { ExposedR2dbcTransactionWrapper(get()) }

}