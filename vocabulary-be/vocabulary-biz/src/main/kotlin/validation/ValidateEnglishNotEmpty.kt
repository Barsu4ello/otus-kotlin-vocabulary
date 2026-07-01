package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorValidation
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.cor.worker


fun ICorChainDsl<VcblContext>.validateEnglishNotEmpty(title: String) = worker {
    this.title = title
    on { wordValidating.english.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "english",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}