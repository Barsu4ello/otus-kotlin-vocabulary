group = "ru.gorbunov.vocabulary"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    register("buildInfra") { ->
        dependsOn(
            gradle.includedBuild("vocabulary-other").task(":buildInfra")
        )
    }

    register("buildImages") {
        dependsOn(gradle.includedBuild("vocabulary-be").task(":buildImages"))
    }

    register("e2eTests") { ->
        dependsOn(
            gradle.includedBuild("vocabulary-tests").task(":e2eTests")
        )
    }

    register("check") {
        group = "verification"
        dependsOn(
            gradle.includedBuild("vocabulary-be").task(":check"),
        )
    }
}