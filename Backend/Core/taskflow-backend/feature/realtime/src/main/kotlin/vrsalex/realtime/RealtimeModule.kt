package vrsalex.realtime

import org.koin.dsl.bind
import org.koin.dsl.module
import vrsalex.core.routing.AppRoute
import vrsalex.realtime.data.RealtimeBridge
import vrsalex.realtime.data.WebSocketPublisher
import vrsalex.realtime.domain.RealtimeEventPublisher
import vrsalex.realtime.web.RealtimeRoute

val realtimeModule = module {

    single { WebSocketPublisher() }

    single<RealtimeEventPublisher> { get<WebSocketPublisher>() }

    single(createdAtStart = true) { RealtimeBridge(get(), get()) }

    single { RealtimeRoute() } bind AppRoute::class
}