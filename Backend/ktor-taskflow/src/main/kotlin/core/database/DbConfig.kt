package vrsalex.core.database

data class DbConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val dbName: String
)
