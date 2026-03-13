package vrsalex.core.database.connection

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import java.time.Duration

object R2dbcFactory {
    fun createDatabase(config: DbConfig): R2dbcDatabase {
        val pgConfig = PostgresqlConnectionConfiguration.builder()
            .host(config.host)
            .port(config.port)
            .database(config.dbName)
            .username(config.user)
            .password(config.password)
            .build()

        val pgFactory: ConnectionFactory = PostgresqlConnectionFactory(pgConfig)

        val poolConfig = ConnectionPoolConfiguration.builder(pgFactory)
            .maxSize(config.poolConnectionMaxCount)
            .initialSize(config.poolConnectionCount)
            .maxIdleTime(Duration.ofMinutes(10))
            .maxLifeTime(Duration.ofMinutes(30))
            .maxAcquireTime(Duration.ofSeconds(10))
            .build()

        val pool = ConnectionPool(poolConfig)

        return R2dbcDatabase.connect(
            connectionFactory = pool,
            databaseConfig = R2dbcDatabaseConfig {
                useNestedTransactions = true
                explicitDialect = PostgreSQLDialect()
            }
        )
    }
}