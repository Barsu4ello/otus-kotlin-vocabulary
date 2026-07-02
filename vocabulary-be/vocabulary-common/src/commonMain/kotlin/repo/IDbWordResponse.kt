package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblWord

sealed interface IDbWordResponse: IDbResponse<VcblWord>

data class DbWordResponseOk(
    val data: VcblWord
): IDbWordResponse

data class DbWordResponseErr(
    val errors: List<VcblError> = emptyList()
): IDbWordResponse {
    constructor(err: VcblError): this(listOf(err))
}

data class DbWordResponseErrWithData(
    val data: VcblWord,
    val errors: List<VcblError> = emptyList()
): IDbWordResponse {
    constructor(word: VcblWord, err: VcblError): this(word, listOf(err))
}