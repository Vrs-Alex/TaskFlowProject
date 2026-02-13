package vrsalex.infrastructure.database

import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway

object FlywayMigration {

    fun run(config: ApplicationConfig) {
        val host = config.property("db.host").getString()
        val port = config.property("db.port").getString()
        val dbName = config.property("db.name").getString()
        val user = config.property("db.user").getString()
        val pass = config.property("db.password").getString()

        val jdbcUrl = "jdbc:postgresql://$host:$port/$dbName"

        Flyway.configure()
            .dataSource(jdbcUrl, user, pass)
            .driver("org.postgresql.Driver")
            .locations("classpath:db/migration")
            .load()
            .migrate()
    }

}