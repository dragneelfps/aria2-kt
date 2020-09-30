plugins {
    application
    kotlin("jvm") version "1.4.10"
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
    implementation("io.github.dragneelfps:aria2-kt:+")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
}
