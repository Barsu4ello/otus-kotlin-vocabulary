plugins {
    kotlin("jvm")
}

// 1. Настраиваем конфигурацию для получения файла из другого проекта(не использовал но код оставил для примера)
//val resourcesFromLib by configurations.creating {
//    isCanBeResolved = true
//    isCanBeConsumed = false
//}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)

//    resourcesFromLib("${libs.vcbl.dcompose.get()}:resources@zip")

    implementation("ru.gorbunov.vocabulary:vocabulary-api-v1-jackson")
    implementation("ru.gorbunov.vocabulary:vocabulary-api-v1-mappers")
    implementation("ru.gorbunov.vocabulary:vocabulary-stubs")
    implementation("ru.gorbunov.vocabulary:vocabulary-app-common")
    implementation("ru.gorbunov.vocabulary:vocabulary-common")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
}

var severity: String = "MINOR"

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

//(не использовал но код оставил для примера)
//tasks {
//    withType<Test>().configureEach {
//        useJUnitPlatform()
//        dependsOn("extractLibResources")
//    }
//    register<Copy>("extractLibResources") {
//        from(resourcesFromLib.elements.map { it.map { file -> zipTree(file) } })
//        into(layout.buildDirectory.dir("dcompose"))
//    }
//}