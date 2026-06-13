package ru.gorbunov.vocabulary.biz.stubs

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWorkMode
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain

fun ICorChainDsl<VcblContext>.stubs(title: String, block: ICorChainDsl<VcblContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == VcblWorkMode.STUB && state == VcblState.RUNNING }
}