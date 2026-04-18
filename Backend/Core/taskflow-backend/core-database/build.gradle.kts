
group = "com.vrsalex.core-database"
version = "0.0.1"


dependencies {
    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    implementation(libs.exposed.kotlin.datetime)

    testImplementation(kotlin("test"))
}
