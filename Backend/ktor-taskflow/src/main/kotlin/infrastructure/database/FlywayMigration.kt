package vrsalex.infrastructure.database

import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import vrsalex.infrastructure.database.config.DbConfig

class FlywayMigration(private val config: DbConfig) {

    private val logger = LoggerFactory.getLogger(FlywayMigration::class.java)

    fun migrate() {
        val jdbcUrl = "jdbc:postgresql://${config.host}:${config.port}/${config.dbName}"
        logger.info("Flyway → запуск миграций на $jdbcUrl")
        try {
            val flyway = Flyway.configure()
                .dataSource(jdbcUrl, config.user, config.password)
                .driver("org.postgresql.Driver")
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .validateOnMigrate(true)
                .failOnMissingLocations(true)
                .outOfOrder(false)
                .cleanDisabled(true)
                .load()

            val result = flyway.migrate()
            logger.info("Flyway завершил работу → применено ${result.migrationsExecuted} миграций")
        } catch (e: Exception) {
            logger.error("Flyway миграция провалилась → приложение не может стартовать", e)
            throw e
        }
    }
}