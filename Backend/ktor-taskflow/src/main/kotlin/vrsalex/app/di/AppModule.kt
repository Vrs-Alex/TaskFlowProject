package vrsalex.app.di

import vrsalex.feature.account.accountModule


val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    accountModule
)