group = "ru.gorbunov.vocabulary"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}

tasks {
    register("buildInfra") {
        group = "build"
        dependsOn(project(":vocabulary-dcompose").getTasksByName("publish",false))
//        dependsOn(project(":ok-vocabulary-specs").getTasksByName("publish",false))
//        dependsOn(project(":ok-vocabulary-swagger").getTasksByName("buildImages",false))
    }

    register("clean" ) {
        description = "Очистка всех подпроектов"
        group = "build"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("clean", false).also {
                this@register.dependsOn(it)
            }
        }
    }
    register("check" ) {
        description = "Запуск тестов всех подпроектов"
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@register.dependsOn(it)
            }
        }
    }

}