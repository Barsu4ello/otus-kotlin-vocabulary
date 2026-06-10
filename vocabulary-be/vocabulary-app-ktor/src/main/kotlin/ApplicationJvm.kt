package ru.gorbunov.vocabulary.app.ktor

import apiV1Mapper
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.gorbunov.vocabulary.app.ktor.plugins.initAppSettings
import ru.gorbunov.vocabulary.app.ktor.v1.v1Word
import ru.gorbunov.vocabulary.app.ktor.v1.wsHandlerV1

// function with config (application.conf)
fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(
    appSettings: VcblAppSettings = initAppSettings(),
) {
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CallLogging) {
        level = Level.INFO
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }
    install(WebSockets)

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
            v1Word(appSettings)
            webSocket("/ws") {
                wsHandlerV1(appSettings)
            }
        }
    }
}