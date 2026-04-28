plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.vrsalex.app"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {

    implementation(project(":core"))
    implementation(libs.flyway.core)
    // implementation(libs.flyway.database.postgresql)

    // Project modules - Features
    implementation(project(":feature:auth"))

    implementation(project(":feature:area"))
    implementation(project(":feature:tag"))

    implementation(project(":feature:item"))
    implementation(project(":feature:event"))

    implementation(project(":feature:realtime"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.status.page)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.websockets)

    // Koin - Dependency Injection
    implementation(libs.koin.ktor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)
    implementation("io.ktor:ktor-server-forwarded-header:3.4.2")
    testImplementation(kotlin("test"))
}
