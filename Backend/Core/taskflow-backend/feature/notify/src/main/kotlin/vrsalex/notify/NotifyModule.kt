package vrsalex.notify

import org.koin.dsl.module
import vrsalex.notify.data.FcmProvider
import vrsalex.notify.data.NotifyBridge
import vrsalex.notify.domain.NotifyRepository

val notifyModule = module {

    single<FcmProvider> { FcmProvider() }

    single<NotifyRepository> { NotifyRepository(get(), get())  }

    single<NotifyBridge>(createdAtStart = true) { NotifyBridge(get(), get()) }

}