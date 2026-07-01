package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorValidation
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech

fun ICorChainDsl<VcblContext>.validatePartOfSpeech(title: String) = worker {
    this.title = title
    on { wordValidating.partOfSpeech == VcblPartOfSpeech.NONE }
    handle {
        fail(
            errorValidation(
                field = "partOfSpeech",
                violationCode = "badFormat",
                description = "field must contain only valid part of speech values"
            )
        )
    }
}