plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-repo-common"))
    implementation(project(":vocabulary-repo-tests"))

    implementation(libs.coroutines.core)
    implementation(libs.db.cache4k)

    implementation(kotlin("test-junit"))
}