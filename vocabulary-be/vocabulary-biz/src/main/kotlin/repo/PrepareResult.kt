package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWorkMode
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != VcblWorkMode.STUB }
    handle {
        wordResponse = wordRepoDone
        wordsResponse = wordsRepoDone
        state = when (val st = state) {
            VcblState.RUNNING -> VcblState.FINISHING
            else -> st
        }
    }
}