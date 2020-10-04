import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}
group = "me.da"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("io.ktor:ktor-client-cio:1.4.0")

}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
application {
    mainClassName = "MainKt"
}