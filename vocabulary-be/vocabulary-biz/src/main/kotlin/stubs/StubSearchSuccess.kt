package ru.gorbunov.vocabulary.biz.stubs

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.logging.common.LogLevel
import ru.gorbunov.vocabulary.stubs.VcblWordStub

fun ICorChainDsl<VcblContext>.stubSearchSuccess(title: String, corSettings: VcblCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска слова
    """.trimIndent()
    on { stubCase == VcblStubs.SUCCESS && state == VcblState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = VcblState.FINISHING
            wordsResponse.addAll(VcblWordStub.prepareSearchList(wordFilterRequest.searchString, VcblPartOfSpeech.NOUN))
        }
    }
}