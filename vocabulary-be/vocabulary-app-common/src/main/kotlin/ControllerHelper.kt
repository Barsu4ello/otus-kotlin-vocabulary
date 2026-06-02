package ru.gorbunov.vocabulary.app.common

import ru.gorbunov.vocabulary.api.log1.mapper.toLog
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.asVcblError
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblState
import kotlin.reflect.KClass
import kotlin.time.Clock


suspend inline fun <T> IVcblAppSettings.controllerHelper(
    crossinline getRequest: suspend VcblContext.() -> Unit,
    crossinline toResponse: suspend VcblContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = VcblContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = VcblState.FAILING
        ctx.errors.add(e.asVcblError())
        processor.exec(ctx)
        if (ctx.command == VcblCommand.NONE) {
            ctx.command = VcblCommand.READ
        }
        ctx.toResponse()
    }
}