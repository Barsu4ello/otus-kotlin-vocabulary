package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErrWithData
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk

fun ICorChainDsl<VcblContext>.repoUpdate(title: String) = worker {
    this.title = title
    description = "Обновление слова в БД"
    on { state == VcblState.RUNNING }
    handle {
        val request = DbWordRequest(wordRepoPrepare)
        when(val result = wordRepo.updateWord(request)) {
            is DbWordResponseOk -> wordRepoDone = result.data
            is DbWordResponseErr -> fail(result.errors)
            is DbWordResponseErrWithData -> {
                fail(result.errors)
                wordRepoDone = result.data
            }
        }
    }
}