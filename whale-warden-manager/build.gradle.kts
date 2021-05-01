val ktorVersion = "1.5.2"
val dockerJavaVersion = "3.2.8"

val entryClass = "net.axay.whalewarden.ManagerKt"

val kotlinVersion: String by rootProject.extra

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    application
    id ("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "net.axay"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    implementation("com.github.docker-java:docker-java-core:$dockerJavaVersion")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:$dockerJavaVersion")

    implementation("org.slf4j:slf4j-simple:1.7.30")

    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:$kotlinVersion")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlinVersion")
    implementation(project(":whale-warden-script"))
    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
}

tasks.withType<JavaCompile> {
    options.release.set(11)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set(entryClass)

    // until https://github.com/johnrengelman/shadow/issues/609 is fixed
    @Suppress("DEPRECATION")
    mainClassName = entryClass
}
