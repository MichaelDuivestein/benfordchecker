plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "za.co.k0ma"
version = "0.0.1-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    // bundles
    implementation(libs.bundles.koin.main)
    implementation(libs.bundles.ktor.main)
    implementation(project.dependencies.platform(libs.koin.bom))

    // libs
    implementation(libs.commons.math)
    implementation(libs.jackson)

    // testing bundles
    testImplementation(libs.bundles.ktor.test)

    // testing libs
    testImplementation(libs.kotlin.test.junit)
}
