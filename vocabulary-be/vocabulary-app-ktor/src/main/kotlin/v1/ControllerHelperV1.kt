package ru.gorbunov.vocabulary.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.gorbunov.vocabulary.api.v1.models.IRequest
import ru.gorbunov.vocabulary.api.v1.models.IResponse
import ru.gorbunov.vocabulary.app.common.AUTH_HEADER
import ru.gorbunov.vocabulary.app.common.controllerHelper
import ru.gorbunov.vocabulary.app.common.jwt2principal
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.mappers.v1.fromTransport
import ru.gorbunov.vocabulary.mappers.v1.toTransportWord
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: VcblAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = this@processV1.request.header(AUTH_HEADER).jwt2principal()
        fromTransport(receive<Q>())
    },
    { respond(toTransportWord()) },
    clazz,
    logId,
)