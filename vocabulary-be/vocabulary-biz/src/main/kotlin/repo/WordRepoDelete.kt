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

fun ICorChainDsl<VcblContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление слова из БД по ID"
    on { state == VcblState.RUNNING }
    handle {
        val request = DbWordIdRequest(wordRepoPrepare)
        when(val result = wordRepo.deleteWord(request)) {
            is DbWordResponseOk -> wordRepoDone = result.data
            is DbWordResponseErr -> {
                fail(result.errors)
                wordRepoDone = wordRepoRead
            }
            is DbWordResponseErrWithData -> {
                fail(result.errors)
                wordRepoDone = result.data
            }
        }
    }
}