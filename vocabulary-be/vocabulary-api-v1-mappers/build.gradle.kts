plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.vocabularyApiV1Jackson)
    implementation(projects.vocabularyCommon)
    implementation(projects.vocabularyStubs)

    testImplementation(kotlin("test-junit"))
}