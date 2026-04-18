package vrsalex.app.di

import vrsalex.auth.authModule
import vrsalex.core.database.databaseModule
import vrsalex.core.security.securityModule

val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    authModule
)