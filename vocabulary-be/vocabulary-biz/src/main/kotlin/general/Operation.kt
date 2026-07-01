package ru.gorbunov.vocabulary.biz.general

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain

fun ICorChainDsl<VcblContext>.operation(
    title: String,
    command: VcblCommand,
    block: ICorChainDsl<VcblContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == VcblState.RUNNING }
}