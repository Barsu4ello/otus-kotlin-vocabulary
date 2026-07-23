package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.repo.*

class WordRepositoryMock(
    private val invokeCreateWord: (DbWordRequest) -> IDbWordResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeReadWord: (DbWordIdRequest) -> IDbWordResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateWord: (DbWordRequest) -> IDbWordResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteWord: (DbWordIdRequest) -> IDbWordResponse = { DEFAULT_AD_SUCCESS_EMPTY_MOCK },
    private val invokeSearchWord: (DbWordFilterRequest) -> IDbWordsResponse = { DEFAULT_ADS_SUCCESS_EMPTY_MOCK },
): IRepoWord {
    override suspend fun createWord(rq: DbWordRequest): IDbWordResponse {
        return invokeCreateWord(rq)
    }

    override suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse {
        return invokeReadWord(rq)
    }

    override suspend fun updateWord(rq: DbWordRequest): IDbWordResponse {
        return invokeUpdateWord(rq)
    }

    override suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse {
        return invokeDeleteWord(rq)
    }

    override suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse {
        return invokeSearchWord(rq)
    }

    companion object {
        val DEFAULT_AD_SUCCESS_EMPTY_MOCK = DbWordResponseOk(VcblWord())
        val DEFAULT_ADS_SUCCESS_EMPTY_MOCK = DbWordsResponseOk(emptyList())
    }
}