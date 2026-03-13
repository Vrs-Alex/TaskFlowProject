package vrsalex.app.di

import vrsalex.feature.account.accountModule
import vrsalex.feature.workspace.workSpaceModule


val appModules = listOf(
    configModule,
    databaseModule,
    securityModule,
    accountModule,

    workSpaceModule
)