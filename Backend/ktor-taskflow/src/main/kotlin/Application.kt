package vrsalex

import io.ktor.server.application.*
import vrsalex.infrastructure.database.FlywayMigration
import vrsalex.plugin.koinConfiguration
import vrsalex.plugin.routeConfiguration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    koinConfiguration()
    routeConfiguration()
}
