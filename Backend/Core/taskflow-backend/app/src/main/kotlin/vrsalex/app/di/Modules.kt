package vrsalex.app.di

import vrsalex.auth.authModule
import vrsalex.core.database.databaseModule
import vrsalex.core.event_bus.eventBusModule
import vrsalex.core.security.securityModule
import vrsalex.event.eventModule
import vrsalex.item.itemModule
import vrsalex.realtime.realtimeModule

val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    authModule,

    itemModule,
    eventModule,

    eventBusModule,
    realtimeModule
)