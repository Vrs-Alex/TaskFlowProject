package com.vrsalex.taskflow.di.feature

import com.vrsalex.taskflow.data.notify.FcmTokenProvider
import com.vrsalex.taskflow.domain.notify.PushTokenProvider
import org.koin.dsl.module

val notifyModule = module {

    single<PushTokenProvider> { FcmTokenProvider() }

}