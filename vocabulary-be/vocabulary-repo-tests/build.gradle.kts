plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-repo-common"))
    implementation(project(":vocabulary-stubs"))

    implementation(libs.coroutines.core)

    implementation(kotlin("test-junit"))
    implementation(libs.coroutines.test)
}