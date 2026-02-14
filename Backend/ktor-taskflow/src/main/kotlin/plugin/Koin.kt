package vrsalex.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import vrsalex.di.appModules

fun Application.koinConfiguration() {
    install(Koin){
        modules(module { single { environment.config } })
        modules(appModules)
    }
}