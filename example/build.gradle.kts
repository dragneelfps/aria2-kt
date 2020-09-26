import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}

dependencies {
//    implementation(project(":"))
    implementation("io.nooblabs:aria2-kt:0.0.1")
    implementation(Deps.coroutines)
}
