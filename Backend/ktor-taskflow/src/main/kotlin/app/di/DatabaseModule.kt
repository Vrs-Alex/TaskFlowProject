package vrsalex.app.di

import org.koin.dsl.module
import vrsalex.core.database.FlywayMigration
import vrsalex.core.database.R2dbcFactory
import vrsalex.core.database.transaction.ExposedR2dbcTransactionWrapper
import vrsalex.core.database.transaction.TransactionWrapper

val databaseModule = module {

    single { R2dbcFactory.createDatabase(get()) }

    single(createdAtStart = true) {
        FlywayMigration(get()).apply {
            migrate()
        }
    }

    single<TransactionWrapper> { ExposedR2dbcTransactionWrapper(get()) }

}