package ru.gorbunov.vocabulary.biz.stubs

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.stubValidationBadPartOfSpeech(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для части речи
    """.trimIndent()

    on { stubCase == VcblStubs.BAD_PATH_OF_SPEECH && state == VcblState.RUNNING }
    handle {
        fail(
            VcblError(
                group = "validation",
                code = "validation-part-of-speech",
                field = "partOfSpeech",
                message = "Wrong part of speech field"
            )
        )
    }
}