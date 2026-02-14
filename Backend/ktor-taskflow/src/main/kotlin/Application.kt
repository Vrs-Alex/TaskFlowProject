package vrsalex

import io.ktor.server.application.*
import vrsalex.plugin.configureSerialization
import vrsalex.plugin.configureStatusPages
import vrsalex.plugin.configureKoin
import vrsalex.plugin.configureRoute

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureStatusPages()
    configureRoute()

}
