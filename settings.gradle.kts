pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "vocabulary"

includeBuild("lesson")
includeBuild("vocabulary-be")
includeBuild("vocabulary-other")
includeBuild("vocabulary-libs")
