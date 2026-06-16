package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.sync.SyncServiceImpl
import com.vrsalex.taskflow.domain.sync.service.SyncService
import org.koin.dsl.module

val syncModule = module {

    single<SyncService>(createdAtStart = true) { SyncServiceImpl().apply { connect() } }

}