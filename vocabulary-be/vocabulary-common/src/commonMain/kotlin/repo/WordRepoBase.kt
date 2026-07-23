package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.helpers.errorSystem

abstract class WordRepoBase {

    protected suspend fun tryWordMethod(block: suspend () -> IDbWordResponse) = try {
        block()
    } catch (e: Throwable) {
        DbWordResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryWordsMethod(block: suspend () -> IDbWordsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbWordsResponseErr(errorSystem("methodException", e = e))
    }
}