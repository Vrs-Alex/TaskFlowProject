package com.vrsalex.taskflow.di

import androidx.room.Room
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.datasource.AreaLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.EventLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.PendingOperationLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.TagLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.math.sin

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
    single { get<AppDatabase>().pendingOperationDao() }

    single { get<AppDatabase>().areaDao() }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().itemTagDao() }

    single { get<AppDatabase>().itemDao() }
    single { get<AppDatabase>().eventDao() }


    // Local Data Source
    single { PendingOperationLocalDataSource(get()) }

    single { TagLocalDataSource(get()) }

    single { AreaLocalDataSource(get()) }

    single { ItemLocalDataSource(get()) }

    single { EventLocalDataSource(get(), get()) }

}