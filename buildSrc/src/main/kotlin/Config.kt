import Versions.ktorVersion

object Versions {
    const val kotlinVersion = "1.4.10"
    const val ktorVersion = "1.4.0"
}

object Deps {
    object KtorClient {
        const val okhttp = "io.ktor:ktor-client-okhttp:$ktorVersion"
        const val logging = "io.ktor:ktor-client-logging:$ktorVersion"
        const val websockets = "io.ktor:ktor-client-websockets:$ktorVersion"
        const val serialization = "io.ktor:ktor-client-serialization:$ktorVersion"
    }
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2"
    const val okhttp3 = "com.squareup.okhttp3:okhttp:4.9.0"
    const val logbackClassic = "ch.qos.logback:logback-classic:1.2.3"
}
