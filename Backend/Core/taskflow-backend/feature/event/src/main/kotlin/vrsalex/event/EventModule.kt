package vrsalex.event

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRouter
import vrsalex.event.data.EventR2dbcRepository
import vrsalex.event.domain.EventRepository
import vrsalex.event.domain.EventService
import vrsalex.event.web.EventRoute

val eventModule = module {

    single<EventRepository> { EventR2dbcRepository(get()) }

    single<EventService> { EventService(get(), get()) }

    single { EventRoute() } bind AppRouter::class

}