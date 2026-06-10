plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)

    implementation(kotlin("test-junit"))
    implementation(libs.coroutines.test)
}
