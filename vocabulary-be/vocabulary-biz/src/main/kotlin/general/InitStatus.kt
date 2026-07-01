package ru.gorbunov.vocabulary.biz.general

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.initStatus(title: String) = worker {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == VcblState.NONE }
    handle { state = VcblState.RUNNING }
}