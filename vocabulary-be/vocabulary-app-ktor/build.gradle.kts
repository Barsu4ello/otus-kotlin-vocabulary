import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import ru.gorbunov.plugin.DockerBuildTask

plugins {
    id("build-jvm")
    id("build-docker")
    alias(libs.plugins.shadowJar)
}

docker {
    // JVM образ
    images.register("Jvm") {
        buildContext = project.layout.buildDirectory.dir("docker-jvm").get().toString()
        dockerFile = "Dockerfile"
        dependsOnTask = "jar"
        imageName = "${project.name}-jvm"
        imageTag = "${project.version}"
    }
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.yaml)
    implementation(libs.ktor.server.headers.response)
    implementation(libs.ktor.server.headers.caching)
    implementation(libs.ktor.server.headers.default)
    implementation(libs.ktor.server.calllogging)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.websocket)

    implementation(project(":vocabulary-common"))
    implementation(project(":vocabulary-app-common"))
    implementation(project(":vocabulary-biz"))

    implementation(project(":vocabulary-api-v1-jackson"))
    implementation(project(":vocabulary-api-v1-mappers"))
    implementation(project(":vocabulary-repo-cassandra"))
    implementation(project(":vocabulary-repo-common"))
    implementation(project(":vocabulary-repo-inmemory"))
    implementation(project(":vocabulary-repo-stubs"))
    implementation(project(":vocabulary-stubs"))

    implementation("ru.gorbunov.vocabulary.libs:vocabulary-lib-logging-common")
    implementation("ru.gorbunov.vocabulary.libs:vocabulary-lib-logging-logback")
    implementation("ru.gorbunov.vocabulary.libs:vocabulary-lib-logging-socket")

    implementation(kotlin("test-junit"))
    testImplementation(libs.testcontainers.cassandra)
    implementation(libs.ktor.server.test)
    implementation(libs.ktor.client.negotiation)
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Main-Class"] =
                "ru.gorbunov.vocabulary.app.ktor.ApplicationJvmKt"
        }
    }
}

afterEvaluate {
    tasks {
        named("dockerBuildJvm", DockerBuildTask::class) {
            dependsOn(shadowJar)
            group = "docker"
            doFirst {
                copy {
                    from("Dockerfile.jvm") { rename { "Dockerfile" } }
                    from(shadowJar.get().archiveFile.get())
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    println("BUILD CONTEXT: ${buildContext.get()}")
                    into(buildContext)
                }
            }
        }
    }
}