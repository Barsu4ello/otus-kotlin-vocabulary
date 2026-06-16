package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain

fun ICorChainDsl<VcblContext>.validation(block: ICorChainDsl<VcblContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == VcblState.RUNNING }
}