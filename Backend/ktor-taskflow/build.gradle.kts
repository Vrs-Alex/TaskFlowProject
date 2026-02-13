plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "vrsalex"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {


    // Ktor сервер и фичи, Netty server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    // Auth + JWT
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cors) // Cross - ...

    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)

    // Koin — DI
    implementation(libs.koin.ktor)

    // yaml-конфиг (application.yaml)
    implementation(libs.ktor.server.config.yaml)

    // Exposed + R2DBC (non-blocking, coroutines)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)                    // если нужны Entity / IntEntity / DAO-стиль
    implementation(libs.exposed.r2dbc)                  // основной модуль для R2DBC
    // implementation(libs.exposed.jdbc)                // закомментировано — только если нужен fallback на JDBC
    implementation(libs.r2dbc.postgresql)               // R2DBC-драйвер PostgreSQL
    implementation(libs.r2dbc.pool)                     // pull соединений

    // Flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql.jdbc)

    // Логгинг
    implementation(libs.logback.classic)


    // H2 — in-memory БД для тестов / dev
    testImplementation(libs.h2)

    // Тесты Ktor + Kotlin
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
