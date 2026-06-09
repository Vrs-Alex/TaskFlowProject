package vrsalex.notify

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRoute
import vrsalex.notify.data.FcmProvider
import vrsalex.notify.data.NotifyBridge
import vrsalex.notify.data.UserDeviceRepositoryImpl
import vrsalex.notify.domain.NotifyRepository
import vrsalex.notify.domain.UserDeviceRepository
import vrsalex.notify.web.NotifyRoute
import vrsalex.notify.web.NotifyService

val notifyModule = module {

    single<FcmProvider> { FcmProvider() }

    single<NotifyRepository> { NotifyRepository(get(), get())  }

    single<NotifyBridge>(createdAtStart = true) { NotifyBridge(get(), get()) }

    single<UserDeviceRepository> { UserDeviceRepositoryImpl() }

    single<NotifyService>{ NotifyService(get(), get()) }

    single { NotifyRoute(get()) } bind AppRoute::class

}