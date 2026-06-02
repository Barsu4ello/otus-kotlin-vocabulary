import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("build-kmp")
    alias(libs.plugins.crowdproj.generator)
    alias(libs.plugins.kotlinx.serialization)
}

// 1. Настраиваем конфигурацию для получения файла из другого проекта
val specsFromLib by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    specsFromLib("ru.gorbunov.vocabulary:vocabulary-specs:0.0.1:spec@zip")
}

val specDir = layout.buildDirectory.dir("specs")

tasks {
    val extractLibSpecs by registering(Copy::class) {
        dependsOn(specsFromLib)
        // Распаковываем ZIP-файл (он будет единственным в этой конфигурации)
        from(specsFromLib.elements.map { it.map { file -> zipTree(file) } })
        into(specDir)
    }

// 3. Привязываем генерацию к распаковке
    named("openApiGenerate") {
        dependsOn(extractLibSpecs)
    }

    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
        finalizedBy("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
}

crowdprojGenerate {
    packageName.set("${project.group}.api.v1")
    inputSpec.set(specDir.map { it.file("specs-word-log1.yml").asFile.absolutePath })
}


kotlin {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(project(":vocabulary-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }


        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}