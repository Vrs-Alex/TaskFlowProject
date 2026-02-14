plugins {
    kotlin("jvm")

    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "vrsalex"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}