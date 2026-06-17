package com.vrsalex.taskflow.di

import androidx.room.Room
import com.vrsalex.taskflow.data.local.datastore.DataStoreManager
import com.vrsalex.taskflow.data.local.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    single<DataStoreManager> {
        DataStoreManager(androidContext())
    }

}