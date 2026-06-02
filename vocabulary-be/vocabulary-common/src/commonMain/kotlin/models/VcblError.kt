package ru.gorbunov.vocabulary.common.models

import ru.gorbunov.vocabulary.logging.common.LogLevel

data class VcblError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)
