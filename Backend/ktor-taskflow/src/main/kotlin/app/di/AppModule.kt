package vrsalex.app.di

import vrsalex.feature.auth.di.authModule


val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    authModule
)