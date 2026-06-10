package ru.gorbunov.vocabulary.common

import ru.gorbunov.vocabulary.common.ws.IVcblWsSessionRepo
import ru.gorbunov.vocabulary.logging.common.VcblLoggerProvider

data class VcblCorSettings(
    val loggerProvider: VcblLoggerProvider = VcblLoggerProvider(),
    val wsSessions: IVcblWsSessionRepo = IVcblWsSessionRepo.NONE,
    ) {
    companion object {
        val NONE = VcblCorSettings()
    }
}