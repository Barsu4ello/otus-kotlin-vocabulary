pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

rootProject.name = "vocabulary"

includeBuild("lesson")
includeBuild("vocabulary-be")
includeBuild("vocabulary-other")
