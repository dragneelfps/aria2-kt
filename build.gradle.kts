plugins {
    kotlin("multiplatform") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
    id("org.jetbrains.dokka") version Versions.dokkaVersion
}

group = "io.github.dragneelfps"
version = Ci.version

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
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
        }
    }

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

apply(plugin = "dokka-config")
apply(plugin = "publish-config")