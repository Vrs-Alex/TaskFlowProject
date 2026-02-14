package vrsalex.di

import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module
import vrsalex.infrastructure.database.config.DbConfig

val configModule = module {

    single {
        val config = get<ApplicationConfig>()
        DbConfig(
            host = config.property("db.host").getString(),
            port = config.property("db.port").getString().toInt(),
            user = config.property("db.user").getString(),
            password = config.property("db.password").getString(),
            dbName = config.property("db.db_name").getString()
        )
    }

}