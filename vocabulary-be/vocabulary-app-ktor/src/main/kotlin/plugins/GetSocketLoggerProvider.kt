package ru.gorbunov.vocabulary.app.ktor.plugins

import io.ktor.server.application.*
import ru.gorbunov.vocabulary.logging.common.VcblLoggerProvider
import ru.gorbunov.vocabulary.logging.socket.SocketLoggerSettings
import ru.gorbunov.vocabulary.logging.socket.vcblLoggerSocket

fun Application.getSocketLoggerProvider(): VcblLoggerProvider {
    val loggerSettings = environment.config.config("ktor.socketLogger").let { conf ->
        SocketLoggerSettings(
            host = conf.propertyOrNull("host")?.getString() ?: "127.0.0.1",
            port = conf.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 9002,
        )
    }
    return VcblLoggerProvider { vcblLoggerSocket(it, loggerSettings) }
}