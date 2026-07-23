package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblWord

sealed interface IDbWordsResponse: IDbResponse<List<VcblWord>>

data class DbWordsResponseOk(
    val data: List<VcblWord>
): IDbWordsResponse

@Suppress("unused")
data class DbWordsResponseErr(
    val errors: List<VcblError> = emptyList()
): IDbWordsResponse {
    constructor(err: VcblError): this(listOf(err))
}