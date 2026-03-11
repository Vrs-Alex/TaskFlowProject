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



    /**

    Модули проекта

     */

    implementation(project("shared-api"))





    /**

    Ktor сервер и плагины

    - Core, Netty

    - Negotiation, Serialization

    - Auth, Auth-Jwt

    - Yaml

    - StatusPage, CORS, RateLimit

     */

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



    /**

    Koin — Dependency injection

     */

    implementation(libs.koin.ktor)



    /**

    Exposed ORM + R2DBC + Postgresql

    Flyway migration on JDBC

     */

    implementation(libs.exposed.core)

    implementation(libs.exposed.r2dbc)

    implementation(libs.exposed.kotlin.datetime)

    implementation(libs.exposed.dao)

    implementation(libs.r2dbc.postgresql)

    implementation(libs.r2dbc.pool)

    implementation(libs.flyway.core)

    implementation(libs.flyway.database.postgresql)

    implementation(libs.postgresql.jdbc)



    /** Kotlin Serialization */

    implementation(libs.kotlinx.serialization.json)



    /** Caffeine for cache in memory */
    implementation(libs.caffeine)



    /** Hash for passwords */

    implementation(libs.mindrot.jbcrypt)



    /** Terminal, logs, etc. */

    implementation(libs.logback.classic)





    /** Test  */

    testImplementation(libs.ktor.server.test.host)

    testImplementation(libs.kotlin.test.junit)

}



tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
    }
}