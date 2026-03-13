group = "vrsalex"
version = "0.0.1"

dependencies {
    // API: Эти либы будут доступны во всех фичах автоматически
    api(project(":shared-api"))
    api(libs.kotlinx.serialization.json)
    api(libs.ktor.server.core) // Чтобы фичи знали Route

    // Database API: Фичи должны уметь работать с таблицами
    api(libs.exposed.core)
    api(libs.exposed.r2dbc)
    api(libs.exposed.kotlin.datetime)
    api(libs.exposed.dao)

    // DI
    api(libs.koin.ktor)

    // IMPLEMENTATION: Скрываем детали реализации от фич
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.status.page)
    implementation(libs.mindrot.jbcrypt)

    // DB Drivers (фичам не нужны драйверы, только интерфейс Exposed)
    implementation(libs.r2dbc.postgresql)
    implementation(libs.r2dbc.pool)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql.jdbc)

    implementation(libs.logback.classic)
}
