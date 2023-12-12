import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.9.20"
}

group = "com.der.bot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "Sonatype Snapshots (Legacy)"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven {
        name = "Sonatype Snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kord)
    implementation(libs.kord.extensions)
    implementation(libs.slf4j)
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.example.bot.AppKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

