package vrsalex.core.event_bus

import org.koin.dsl.module

val eventBusModule = module {

    single<EventBus> { EventBus() }

}