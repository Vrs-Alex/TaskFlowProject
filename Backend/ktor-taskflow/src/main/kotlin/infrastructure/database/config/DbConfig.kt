package vrsalex.infrastructure.database.config

data class DbConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val dbName: String
)
