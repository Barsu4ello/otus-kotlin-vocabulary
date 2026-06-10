package ru.gorbunov.vocabulary.app.ktor.v1

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings

fun Route.v1Word(appSettings: VcblAppSettings) {
    route("word") {
        post("create") {
            call.createWord(appSettings)
        }
        post("read") {
            call.readWord(appSettings)
        }
        post("update") {
            call.updateWord(appSettings)
        }
        post("delete") {
            call.deleteWord(appSettings)
        }
        post("search") {
            call.searchWord(appSettings)
        }
    }
}