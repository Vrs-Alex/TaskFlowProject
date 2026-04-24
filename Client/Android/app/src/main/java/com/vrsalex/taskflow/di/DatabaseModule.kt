package com.vrsalex.taskflow.di

import androidx.room.Room
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.datasource.EventLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "database"
        ).build()
    }

    // Dao

    single { get<AppDatabase>().syncDao() }

    single { get<AppDatabase>().areaDao() }
    single { get<AppDatabase>().tagDao() }

    single { get<AppDatabase>().itemDao() }
    single { get<AppDatabase>().eventDao() }


    // Local Data Source

    single { EventLocalDataSource(get()) }


}