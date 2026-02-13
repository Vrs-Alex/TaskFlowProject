package vrsalex.di

import org.koin.dsl.module
import vrsalex.infrastructure.database.R2dbcFactory

val databaseModule = module {
    single { R2dbcFactory.createDatabase(get()) }

}