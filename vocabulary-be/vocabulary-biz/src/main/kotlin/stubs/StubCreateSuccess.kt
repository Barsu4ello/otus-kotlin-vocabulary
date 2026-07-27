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

fun ICorChainDsl<VcblContext>.stubCreateSuccess(title: String, corSettings: VcblCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания слова
    """.trimIndent()
    on { stubCase == VcblStubs.SUCCESS && state == VcblState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = VcblState.FINISHING
            val stub = VcblWordStub.prepareResult {
                wordRequest.english.takeIf { it.isNotBlank() }?.also { this.english = it }
                wordRequest.russian.takeIf { it.isNotBlank() }?.also { this.russian = it }
                wordRequest.partOfSpeech.takeIf { it != VcblPartOfSpeech.NONE }?.also { this.partOfSpeech = it }
            }
            wordResponse = stub
        }
    }
}