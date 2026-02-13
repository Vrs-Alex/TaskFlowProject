package vrsalex

import io.ktor.server.application.*
import io.ktor.server.config.property
import io.ktor.server.routing.routing
import org.koin.core.Koin
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.koinModules
import vrsalex.di.databaseModule
import vrsalex.infrastructure.database.FlywayMigration
import vrsalex.plugins.koinConfiguration

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    FlywayMigration.run(environment.config)
    koinConfiguration()

}
