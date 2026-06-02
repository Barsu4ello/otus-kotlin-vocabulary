package ru.gorbunov.vocabulary.app.ktor.plugins

import io.ktor.server.application.Application
import ru.gorbunov.vocabulary.logging.common.VcblLoggerProvider
import ru.gorbunov.vocabulary.logging.jvm.vcblLoggerLogback

fun Application.getLoggerProviderConf(): VcblLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "socket", "sock" -> getSocketLoggerProvider()
        "logback", null -> VcblLoggerProvider { vcblLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
    }