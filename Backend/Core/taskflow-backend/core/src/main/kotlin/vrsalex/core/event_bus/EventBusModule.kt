package vrsalex.core.event_bus

import org.koin.dsl.module
import kotlin.math.sin

val eventBusModule = module {

    single<EventBus> { EventBus() }

}