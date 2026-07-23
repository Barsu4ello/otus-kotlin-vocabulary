package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == VcblState.RUNNING }
    handle {
        wordRepoPrepare = wordValidated.copy()
        // TODO будет реализовано в занятии по управлению пользвателями
        wordRepoPrepare.ownerId = VcblUserId.NONE
    }
}