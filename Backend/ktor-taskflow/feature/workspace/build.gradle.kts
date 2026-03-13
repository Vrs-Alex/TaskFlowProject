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


    testImplementation(kotlin("test"))
}