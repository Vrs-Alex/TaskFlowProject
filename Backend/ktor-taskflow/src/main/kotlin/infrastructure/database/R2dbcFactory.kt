package vrsalex.infrastructure.database

import io.ktor.server.config.ApplicationConfig
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import java.time.Duration

object R2dbcFactory {
    fun createDatabase(config: ApplicationConfig): R2dbcDatabase {
        val pgConfig = PostgresqlConnectionConfiguration.builder()
            .host(config.property("db.host").getString())
            .port(config.property("db.port").getString().toInt())
            .database(config.property("db.name").getString())
            .username(config.property("db.user").getString())
            .password(config.property("db.password").getString())
            .build()

        val pgFactory: ConnectionFactory = PostgresqlConnectionFactory(pgConfig)

        val poolConfig = ConnectionPoolConfiguration.builder(pgFactory)
            .maxSize(20)
            .initialSize(5)
            .maxIdleTime(Duration.ofMinutes(10))
            .maxLifeTime(Duration.ofMinutes(30))
            .maxAcquireTime(Duration.ofSeconds(30))
            .validationQuery("SELECT 1")  // рекомендуется для health-check
            .build()

        val pool = ConnectionPool(poolConfig)

        return R2dbcDatabase.connect(
            connectionFactory = pool,
            databaseConfig = R2dbcDatabaseConfig {}
        )
    }
}