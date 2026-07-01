package ru.gorbunov.vocabulary.biz.stubs

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker


fun ICorChainDsl<VcblContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == VcblState.RUNNING }
    handle {
        fail(
            VcblError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}