//plugins {
//    kotlin("jvm")
//}

group = "com.vrsalex.auth"
version = "0.0.1"



dependencies {
    implementation(project(":core"))
    implementation(libs.mindrot.jbcrypt)
    implementation(libs.caffeine)
    implementation(libs.ktor.server.auth.jwt)

    testImplementation(kotlin("test"))
}

