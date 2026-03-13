package vrsalex.app

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import vrsalex.app.plugin.configureAuth
import vrsalex.app.plugin.configureSerialization
import vrsalex.app.plugin.configureStatusPages
import vrsalex.app.plugin.configureKoin
import vrsalex.app.plugin.configureRateLimit
import vrsalex.app.plugin.configureRoute

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureRateLimit()
    configureSerialization()
    configureAuth()
    configureStatusPages()
    configureRoute()
}
