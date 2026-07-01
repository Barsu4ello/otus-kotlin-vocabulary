package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorValidation
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.cor.worker


fun ICorChainDsl<VcblContext>.validateRussianNotEmpty(title: String) = worker {
    this.title = title
    on { wordValidating.russian.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "russian",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}