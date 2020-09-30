apply(plugin = "maven-publish")
apply(plugin = "signing")

val dokkaJavadocJar by tasks.existing
val dokkaHtmlJar by tasks.existing

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            url = if (version.toString().endsWith("SNAPSHOT"))
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            else
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifact(dokkaJavadocJar.get())
            artifact(dokkaHtmlJar.get())
            from(components["java"])
            pom {
                name.set("Aria2.kt")
                description.set("Aria2 Client written in Kotlin")
                url.set("https://github.com/dragneelfps/aria2-kt")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("dragneelfps")
                        name.set("Sourabh S Rawat")
                        email.set("dragneelfps@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/dragneelfps/aria2-kt.git")
                    developerConnection.set("scm:git:ssh://github.com/dragneelfps/aria2-kt.git")
                    url.set("https://github.com/dragneelfps/aria2-kt.kt")
                }
            }
        }
    }
}

extensions.configure<SigningExtension> {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(extensions.findByType<PublishingExtension>()!!.publications)
}
