import org.jetbrains.kotlin.gradle.internal.config.AnalysisFlags.optIn

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

    implementation(project("shared-api"))

    // Ktor сервер и плагины (negotiation, serialization, netty, auth jwt, etc.)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.status.page)
    implementation(libs.ktor.server.cors) // Cross - ...
    implementation(libs.ktor.server.rate.limit)

    // Koin — DI
    implementation(libs.koin.ktor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Exposed + R2DBC
    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    implementation(libs.r2dbc.postgresql)
    implementation(libs.r2dbc.pool)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.dao)
    // implementation(libs.exposed.jdbc) //  fallback на JDBC

    // Flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql.jdbc)

    implementation(libs.mindrot.jbcrypt)

    // Логгинг
    implementation(libs.logback.classic)


    // Тесты Ktor + Kotlin
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}



tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }
}