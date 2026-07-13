plugins {
    id("build-jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.vocabularyCommon)
    implementation(projects.vocabularyRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)

    testImplementation(projects.vocabularyRepoTests)
    testImplementation(libs.testcontainers.cassandra)
    testImplementation(libs.logback)
    testImplementation(kotlin("test-junit"))
}