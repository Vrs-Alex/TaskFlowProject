package vrsalex.app.di

import feature.account.di.accountModule


val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    accountModule
)