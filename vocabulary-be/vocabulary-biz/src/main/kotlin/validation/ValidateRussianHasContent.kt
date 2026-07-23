package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorValidation
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.validateRussianHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("^[а-яА-ЯёЁ]+$")
    on { wordValidating.russian.isNotEmpty() && !wordValidating.russian.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "russian",
                violationCode = "noContent",
                description = "field must contain only russian letters"
            )
        )
    }
}