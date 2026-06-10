package ru.gorbunov.vocabulary.logging.socket

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import ru.gorbunov.vocabulary.logging.common.IVcblLogWrapper
import kotlin.reflect.KClass

data class SocketLoggerSettings(
    val host: String = "127.0.0.1",
    val port: Int = 9002,
    val emitToStdout: Boolean = true,
    val bufferSize: Int = 16,
    val overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
)

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
fun vcblLoggerSocket(
    loggerId: String,
    settings: SocketLoggerSettings = SocketLoggerSettings()
): IVcblLogWrapper = VcblLoggerWrapperSocket(
    loggerId = loggerId,
    host = settings.host,
    port = settings.port,
    emitToStdout = settings.emitToStdout,
    bufferSize = settings.bufferSize,
    overflowPolicy = settings.overflowPolicy,
    scope = settings.scope,
)

@Suppress("unused")
fun vcblLoggerSocket(cls: KClass<*>, settings: SocketLoggerSettings = SocketLoggerSettings()): IVcblLogWrapper = vcblLoggerSocket(
    loggerId = cls.qualifiedName ?: "",
    settings = settings,
)