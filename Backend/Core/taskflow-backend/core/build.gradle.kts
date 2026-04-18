plugins {
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.vrsalex.core"
version = "0.0.1"

dependencies {
    api(project(":core-database"))
    api(project(":shared-api"))

    api(libs.kotlinx.serialization.json)
    api(libs.ktor.server.core)

    api(libs.exposed.core)
    api(libs.exposed.r2dbc)
    api(libs.exposed.kotlin.datetime)

    api(libs.koin.ktor)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.status.page)
    implementation(libs.mindrot.jbcrypt)

    implementation(libs.r2dbc.postgresql)

    implementation(libs.r2dbc.pool)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql.jdbc)

    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)
    testImplementation(kotlin("test"))
}
