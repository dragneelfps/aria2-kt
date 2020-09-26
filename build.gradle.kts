import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
    `maven-publish`
}

group = "io.nooblabs"
version = "0.0.1"

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(Deps.KtorClient.okhttp)
    implementation(Deps.KtorClient.logging)
    implementation(Deps.KtorClient.websockets)
    implementation(Deps.KtorClient.serialization)
    implementation(Deps.coroutines)
    implementation(Deps.serialization)
    implementation(Deps.okhttp3)
    implementation(Deps.logbackClassic)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
        }
    }
}
