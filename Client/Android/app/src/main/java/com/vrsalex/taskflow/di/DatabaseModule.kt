package com.vrsalex.taskflow.di

import androidx.room.Room
import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.local.db.datasource.workspace.AreaLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.EventLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.ItemLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.sync.PendingOperationLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.workspace.TagLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLocalDataSource
import com.vrsalex.taskflow.data.local.db.datasource.item.TaskLogLocalDataSource
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
    single { get<AppDatabase>().pendingOperationDao() }

    single { get<AppDatabase>().areaDao() }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().itemTagDao() }

    single { get<AppDatabase>().itemDao() }
    single { get<AppDatabase>().eventDao() }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().taskLogDao() }


    // Local Data Source
    single { PendingOperationLocalDataSource(get()) }

    single { TagLocalDataSource(get()) }

    single { AreaLocalDataSource(get()) }

    single { ItemLocalDataSource(get()) }

    single { EventLocalDataSource(get(), get()) }

    single { TaskLocalDataSource(get(), get()) }

    single { TaskLogLocalDataSource(get()) }


}