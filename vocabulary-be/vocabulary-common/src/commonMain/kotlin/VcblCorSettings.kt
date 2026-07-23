package ru.gorbunov.vocabulary.common

import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.common.ws.IVcblWsSessionRepo
import ru.gorbunov.vocabulary.logging.common.VcblLoggerProvider

data class VcblCorSettings(
    val loggerProvider: VcblLoggerProvider = VcblLoggerProvider(),
    val wsSessions: IVcblWsSessionRepo = IVcblWsSessionRepo.NONE,
    val repoStub: IRepoWord = IRepoWord.NONE,
    val repoTest: IRepoWord = IRepoWord.NONE,
    val repoProd: IRepoWord = IRepoWord.NONE,
    ) {
    companion object {
        val NONE = VcblCorSettings()
    }
}