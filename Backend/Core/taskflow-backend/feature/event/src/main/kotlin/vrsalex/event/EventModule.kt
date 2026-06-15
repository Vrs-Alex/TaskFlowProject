package vrsalex.event

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRoute
import vrsalex.event.data.EventR2dbcRepository
import vrsalex.event.domain.EventConverter
import vrsalex.event.domain.EventRepository
import vrsalex.event.domain.EventService
import vrsalex.event.web.EventRoute
import vrsalex.item.domain.conversion.SubItemConverter

val eventModule = module {

    single<EventRepository> { EventR2dbcRepository(get()) }

    single<EventService> { EventService(get(), get(), get()) }

    single { EventConverter(get()) } bind SubItemConverter::class

    single { EventRoute() } bind AppRoute::class

}