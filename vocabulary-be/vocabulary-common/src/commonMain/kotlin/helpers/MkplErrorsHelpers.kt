package ru.gorbunov.vocabulary.common.helpers

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblState

fun Throwable.asVcblError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = VcblError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun VcblContext.addError(vararg error: VcblError) = errors.addAll(error)

inline fun VcblContext.fail(error: VcblError) {
    addError(error)
    state = VcblState.FAILING
}