package vrsalex.app.di

import io.ktor.server.config.ApplicationConfig
import org.koin.dsl.module
import vrsalex.core.database.DbConfig
import core.security.JwtConfig

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

    single {
        val config = get<ApplicationConfig>()
        JwtConfig(
            issuer = config.property("jwt.domain").getString(),
            audience = config.property("jwt.audience").getString(),
            expiration = config.property("jwt.expiration").getString().toLong(),
            refreshExpiration = config.property("jwt.refreshExpiration").getString().toLong(),
            realm = config.property("jwt.realm").getString(),
            secret = config.property("jwt.secret").getString()
        )
    }

}