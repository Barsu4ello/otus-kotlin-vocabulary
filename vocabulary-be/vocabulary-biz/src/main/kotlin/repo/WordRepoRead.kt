package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.repo.DbWordIdRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErrWithData
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail

fun ICorChainDsl<VcblContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение слова из БД"
    on { state == VcblState.RUNNING }
    handle {
        val request = DbWordIdRequest(wordValidated)
        when(val result = wordRepo.readWord(request)) {
            is DbWordResponseOk -> wordRepoRead = result.data
            is DbWordResponseErr -> fail(result.errors)
            is DbWordResponseErrWithData -> {
                fail(result.errors)
                wordRepoRead = result.data
            }
        }
    }
}