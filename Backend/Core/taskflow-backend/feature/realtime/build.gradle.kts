

group = "com.vrsalex"
version = "0.0.1"


dependencies {

    implementation(project(":core"))

    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.auth)

    testImplementation(kotlin("test"))
}

