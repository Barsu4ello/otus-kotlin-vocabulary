package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.finishWordValidation(title: String) = worker {
    this.title = title
    on { state == VcblState.RUNNING }
    handle {
        wordValidated = wordValidating
    }
}

fun ICorChainDsl<VcblContext>.finishWordFilterValidation(title: String) = worker {
    this.title = title
    on { state == VcblState.RUNNING }
    handle {
        wordFilterValidated = wordFilterValidating
    }
}