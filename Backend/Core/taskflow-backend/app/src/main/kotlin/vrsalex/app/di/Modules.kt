package vrsalex.app.di

import vrsalex.area.areaModule
import vrsalex.auth.authModule
import vrsalex.core.database.databaseModule
import vrsalex.core.event_bus.eventBusModule
import vrsalex.core.security.securityModule
import vrsalex.event.eventModule
import vrsalex.item.itemModule
import vrsalex.realtime.realtimeModule
import vrsalex.tag.tagModule
import vrsalex.task.taskModule

val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    authModule,

    areaModule,
    tagModule,

    itemModule,
    eventModule,
    taskModule,

    eventBusModule,
    realtimeModule
)