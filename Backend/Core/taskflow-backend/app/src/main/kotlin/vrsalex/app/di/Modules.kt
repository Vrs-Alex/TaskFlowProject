package vrsalex.app.di

import vrsalex.auth.authModule
import vrsalex.core.database.databaseModule
import vrsalex.core.security.securityModule
import vrsalex.event.eventModule
import vrsalex.item.itemModule

val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    authModule,

    itemModule,
    eventModule
)