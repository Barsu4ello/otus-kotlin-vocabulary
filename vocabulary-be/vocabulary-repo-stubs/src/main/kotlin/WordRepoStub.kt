package ru.gorbunov.vocabulary.backend.repository.inmemory

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.repo.*
import ru.gorbunov.vocabulary.stubs.VcblWordStub


class WordRepoStub() : IRepoWord {
    override suspend fun createWord(rq: DbWordRequest): IDbWordResponse {
        return DbWordResponseOk(
            data = VcblWordStub.get(),
        )
    }

    override suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse {
        return DbWordResponseOk(
            data = VcblWordStub.get(),
        )
    }

    override suspend fun updateWord(rq: DbWordRequest): IDbWordResponse {
        return DbWordResponseOk(
            data = VcblWordStub.get(),
        )
    }

    override suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse {
        return DbWordResponseOk(
            data = VcblWordStub.get(),
        )
    }

    override suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse {
        return DbWordsResponseOk(
            data = VcblWordStub.prepareSearchList(filter = "", VcblPartOfSpeech.NOUN),
        )
    }
}