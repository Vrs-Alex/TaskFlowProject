plugins {
    alias(libs.plugins.ktor)
}

group = "vrsalex"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}


dependencies {
    implementation(project(":core"))

    // Project modules - Features
    implementation(project(":feature:account"))
    implementation(project(":feature:workspace"))
    implementation(project(":feature:task"))
    implementation(project(":feature:habit"))
    implementation(project(":feature:goal"))
    implementation(project(":feature:event"))
    implementation(project(":feature:reminder"))
    implementation(project(":feature:attachment"))

    // Ktor server and plugins
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

    // Koin - Dependency Injection
    implementation(libs.koin.ktor)


    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)


    // Logging
    implementation(libs.logback.classic)

    // Test
    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}


