plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(libs.cor)

    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-stubs"))
    implementation(project(":vocabulary-auth"))

    testImplementation(project(":vocabulary-repo-common"))
    testImplementation(project(":vocabulary-repo-tests"))
    testImplementation(project("::vocabulary-repo-inmemory"))

    testImplementation(kotlin("test-junit"))
    testImplementation(libs.coroutines.test)
}

