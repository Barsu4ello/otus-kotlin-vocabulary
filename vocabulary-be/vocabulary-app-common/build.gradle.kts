plugins {
    id("build-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)
    implementation(libs.jackson.kotlin)


    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-api-log1"))
    implementation(project(":vocabulary-api-v1-kmp"))
    implementation(project(":vocabulary-biz"))

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)

}
