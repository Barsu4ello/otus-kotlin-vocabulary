plugins {
    kotlin("jvm") apply false
}

group = "ru.otus.otuskotlin.vocabulary"
version = "0.0.1"

subprojects {

    repositories {
        mavenCentral()
    }

    group = rootProject.group
    version =  rootProject.version
}
