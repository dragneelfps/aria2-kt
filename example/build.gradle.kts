plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

application {
    mainClassName = "MainKt"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
    implementation(project(":"))
    implementation(Deps.KtorClient.okhttp)
    implementation(Deps.KtorClient.logging)
    implementation(Deps.KtorClient.websockets)
    implementation(Deps.KtorClient.serialization)
    implementation(Deps.coroutines)
    implementation(Deps.serialization)
    implementation(Deps.okhttp3)
    implementation(Deps.logbackClassic)
}
