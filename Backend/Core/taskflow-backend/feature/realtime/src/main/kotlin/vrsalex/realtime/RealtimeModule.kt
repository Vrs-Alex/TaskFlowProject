package vrsalex.realtime

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.core.routing.AppRoute
import vrsalex.realtime.data.RealtimeBridge
import vrsalex.realtime.data.WebSocketRealtimePublisher
import vrsalex.realtime.web.RealtimeRoute

val realtimeModule = module {

    single { WebSocketRealtimePublisher() }

    single<RealtimeEventPublisher> { get<WebSocketRealtimePublisher>() }

    single(createdAtStart = true) { RealtimeBridge(get(), get(), get()) }

    single { RealtimeRoute() } bind AppRoute::class
}