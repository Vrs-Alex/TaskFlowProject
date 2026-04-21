package vrsalex.realtime

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.event_bus.RealtimeEventPublisher
import vrsalex.core.routing.AppRouter

val realtimeModule = module {

    single { WebSocketSessionManager() }

    single<RealtimeEventPublisher> { get<WebSocketSessionManager>() }

    single(createdAtStart = true) { RealtimeBridge(get(), get()) }

    single { RealtimeRoute() } bind AppRouter::class
}