package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.local.db.AppDatabase
import com.vrsalex.taskflow.data.sync.SyncCursorStoreImpl
import com.vrsalex.taskflow.data.sync.SyncPuller
import com.vrsalex.taskflow.data.realtime.RealtimeServiceImpl
import com.vrsalex.taskflow.domain.sync.repository.SyncCursorStore
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import com.vrsalex.taskflow.domain.sync.service.SyncService
import org.koin.dsl.module

val syncModule = module {

    single<SyncCursorStore> { SyncCursorStoreImpl(get<AppDatabase>().syncCursorDao()) }
    single { SyncPuller(get()) }

    single { SyncService(get(), get(), get(), get(), get(), get()) }

}