package ru.gorbunov.vocabulary.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * Инициализирует выбранный логер
 *
 * ```kotlin
 * // Обычно логер вызывается вот так
 * val logger = LoggerFactory.getLogger(this::class.java)
 * // Мы создаем экземпляр логер-провайдера вот так
 * val loggerProvider = VcblLoggerProvider { clazz -> vcblLoggerLogback(clazz) }
 *
 * // В дальнейшем будем использовать этот экземпляр вот так:
 * val logger = loggerProvider.logger(this::class)
 * logger.info("My log")
 * ```
 */
class VcblLoggerProvider(
    private val provider: (String) -> IVcblLogWrapper = { IVcblLogWrapper.DEFAULT }
) {
    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(loggerId: String): IVcblLogWrapper = provider(loggerId)

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(clazz: KClass<*>): IVcblLogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    /**
     * Инициализирует и возвращает экземпляр логера
     */
    fun logger(function: KFunction<*>): IVcblLogWrapper = provider(function.name)
}