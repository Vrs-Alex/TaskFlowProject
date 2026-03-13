
group = "vrsalex"
version = "0.0.1"


dependencies {

    implementation(libs.exposed.core)
    implementation(libs.exposed.r2dbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.dao)

    testImplementation(kotlin("test"))
}
