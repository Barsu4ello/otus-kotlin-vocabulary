package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseOk
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail

fun ICorChainDsl<VcblContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск слов в БД по фильтру"
    on { state == VcblState.RUNNING }
    handle {
        val request = DbWordFilterRequest(
            searchString = wordFilterValidated.searchString,
            ownerId = wordFilterValidated.ownerId,
            partOfSpeech = wordFilterValidated.partOfSpeech,
        )
        when(val result = wordRepo.searchWord(request)) {
            is DbWordsResponseOk -> wordsRepoDone = result.data.toMutableList()
            is DbWordsResponseErr -> fail(result.errors)
        }
    }
}