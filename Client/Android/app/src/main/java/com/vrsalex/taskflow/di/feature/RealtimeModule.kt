package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.realtime.RealtimeServiceImpl
import com.vrsalex.taskflow.domain.realtime.RealtimeService
import org.koin.dsl.module

val realtimeModule = module {

    // Start
    single<RealtimeService> { RealtimeServiceImpl(get(), get(), get()) }

}