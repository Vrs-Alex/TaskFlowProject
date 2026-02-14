package vrsalex.di

import org.koin.dsl.module
import vrsalex.infrastructure.database.FlywayMigration
import vrsalex.infrastructure.database.R2dbcFactory

val databaseModule = module {

    single { R2dbcFactory.createDatabase(get()) }

    single(createdAtStart = true) {
        FlywayMigration(get()).apply {
            migrate()
        }
    }

}