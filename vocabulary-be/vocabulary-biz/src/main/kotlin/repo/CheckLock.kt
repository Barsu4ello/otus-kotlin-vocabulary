package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.repo.errorRepoConcurrency

fun ICorChainDsl<VcblContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == VcblState.RUNNING && wordValidated.lock != wordRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(wordRepoRead, wordValidated.lock).errors)
    }
}