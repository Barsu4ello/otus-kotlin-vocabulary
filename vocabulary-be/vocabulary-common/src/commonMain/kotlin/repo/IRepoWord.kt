package ru.gorbunov.vocabulary.common.repo

interface  IRepoWord {

    suspend fun createWord(rq: DbWordRequest): IDbWordResponse
    suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse
    suspend fun updateWord(rq: DbWordRequest): IDbWordResponse
    suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse
    suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse

    companion object {
        val NONE = object : IRepoWord {
            override suspend fun createWord(rq: DbWordRequest): IDbWordResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateWord(rq: DbWordRequest): IDbWordResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}