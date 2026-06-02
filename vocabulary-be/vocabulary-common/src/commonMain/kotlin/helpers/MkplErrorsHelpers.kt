package ru.gorbunov.vocabulary.common.helpers

import ru.gorbunov.vocabulary.common.models.VcblError

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