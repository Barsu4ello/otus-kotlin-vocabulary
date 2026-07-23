rootProject.name = "vocabulary-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("test-module")
include("vocabulary-api-v1-jackson")
include("vocabulary-api-v1-kmp")
include("vocabulary-common")
include("vocabulary-api-v1-mappers")
include("vocabulary-stubs")
include("vocabulary-app-common")
include("vocabulary-app-ktor")
include("vocabulary-biz")
include("vocabulary-api-log1")
include("vocabulary-repo-inmemory")
include("vocabulary-repo-common")
include("vocabulary-repo-tests")
include("vocabulary-repo-stubs")
include("vocabulary-repo-cassandra")





