package vrsalex.app.plugin

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import vrsalex.app.di.appModules

fun Application.configureKoin() {
    install(Koin){
        modules(module { single { environment.config } })
        modules(appModules)
    }
}