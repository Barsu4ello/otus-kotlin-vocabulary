package ru.gorbunov.vocabulary.app.ktor.plugins

import io.ktor.server.application.Application
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.base.KtorWsSessionRepo
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings

fun Application.initAppSettings(): VcblAppSettings {
    val corSettings = VcblCorSettings(
        loggerProvider = getLoggerProviderConf(),
        wsSessions = KtorWsSessionRepo(),
    )
    return VcblAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = VcblWordProcessor(corSettings),
    )
}