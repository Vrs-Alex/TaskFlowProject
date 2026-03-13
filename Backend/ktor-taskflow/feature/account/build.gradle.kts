group = "vrsalex"
version = "0.0.1"


dependencies {

    /**
     * Модуль core пробрасывает через api():
     *
     * Shared-api module (network dto)
     * Koin for DI
     * Kotlin Serialization
     * Ktor Server Core
     * Exposed (core, r2dbc, datetime, dao)
     */
    implementation(project(":core"))

    // Security hash password
    implementation(libs.mindrot.jbcrypt)

    // Caffeine cache
    implementation(libs.caffeine)

    // JWT
    implementation(libs.ktor.server.auth.jwt)

    testImplementation(kotlin("test"))
}