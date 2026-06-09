group = "com.vrsalex"
version = "0.0.1"

dependencies {
    implementation(project(":core"))
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation(libs.lettuce)

    testImplementation(kotlin("test"))
}
