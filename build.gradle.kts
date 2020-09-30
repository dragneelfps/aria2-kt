import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
    id("org.jetbrains.dokka") version Versions.dokkaVersion
    `maven-publish`
    signing
}

group = "io.github.dragneelfps"
version = Ci.version

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(Deps.KtorClient.okhttp)
    implementation(Deps.KtorClient.logging)
    implementation(Deps.KtorClient.websockets)
    implementation(Deps.KtorClient.serialization)
    implementation(Deps.coroutines)
    implementation(Deps.serialization)
    implementation(Deps.okhttp3)
    implementation(Deps.logbackClassic)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

apply(from = "./gradle/dokka.gradle.kts")
apply(from = "./gradle/publish.gradle.kts")
