package vrsalex.app

import io.ktor.server.application.*
import io.ktor.server.netty.*
import vrsalex.app.plugin.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureHTTP()
    configureRateLimit()
    configureSerialization()
    configureAuth()
    configureStatusPages()
    configureWebSocket()
    configureRoute()
}
