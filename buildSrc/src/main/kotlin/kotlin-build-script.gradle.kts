plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

group = "net.axay"
version = "0.0.1"

tasks.withType<JavaCompile> {
    options.release.set(8)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
