package ru.gorbunov.vocabulary.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.gorbunov.vocabulary.logging.common.IVcblLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal VcblLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun vcblLoggerLogback(logger: Logger): IVcblLogWrapper = VcblLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun vcblLoggerLogback(clazz: KClass<*>): IVcblLogWrapper = vcblLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

@Suppress("unused")
fun vcblLoggerLogback(loggerId: String): IVcblLogWrapper = vcblLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)