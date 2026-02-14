package vrsalex

import io.ktor.server.application.*
import vrsalex.infrastructure.database.FlywayMigration
import vrsalex.plugins.koinConfiguration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    koinConfiguration()

}
