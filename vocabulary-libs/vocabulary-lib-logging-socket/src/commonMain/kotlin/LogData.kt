package ru.gorbunov.vocabulary.logging.socket

import kotlinx.serialization.Serializable
import ru.gorbunov.vocabulary.logging.common.LogLevel

@Serializable
data class LogData(
    val level: LogLevel,
    val message: String,
//    val data: T
)