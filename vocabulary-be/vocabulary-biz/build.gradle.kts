plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(libs.cor)

    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-stubs"))

    implementation(kotlin("test-junit"))
    implementation(libs.coroutines.test)
}

