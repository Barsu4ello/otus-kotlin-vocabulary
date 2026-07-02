plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":vocabulary-common"))
    implementation(libs.coroutines.core)
}