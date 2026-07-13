package ru.gorbunov.vocabulary.app.ktor.v1

import apiV1Mapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import ru.gorbunov.vocabulary.api.v1.models.IRequest
import ru.gorbunov.vocabulary.app.common.controllerHelper
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.base.KtorWsSessionV1
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.mappers.v1.fromTransport
import ru.gorbunov.vocabulary.mappers.v1.toTransportInit
import ru.gorbunov.vocabulary.mappers.v1.toTransportWord
import kotlin.reflect.KClass

private val clWsV1: KClass<*> = WebSocketSession::wsHandlerV1::class
suspend fun WebSocketSession.wsHandlerV1(appSettings: VcblAppSettings) = with(KtorWsSessionV1(this)) {
    val sessions = appSettings.corSettings.wsSessions
    sessions.add(this)

    // Handle init request
    appSettings.controllerHelper(
        {
            command = VcblCommand.INIT
            wsSession = this@with
        },
        { outgoing.send(Frame.Text(apiV1Mapper.writeValueAsString(toTransportInit()))) },
        clWsV1,
        "wsV1-init"
    )

    // Handle flow
    incoming.receiveAsFlow().map {
        val frame = it as? Frame.Text ?: return@map
        // Handle without flow destruction
        try {
            appSettings.controllerHelper(
                {
                    val request = apiV1Mapper.readValue<IRequest>(frame.readText())
                    fromTransport(request)
                    wsSession = this@with
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportWord())
                    // If change request, response is sent to everyone
                    outgoing.send(Frame.Text(result))
                },
                clWsV1,
                "wsV1-handle"
            )

        } catch (_: ClosedReceiveChannelException) {
            sessions.remove(this@with)
        } finally {
            // Handle finish request
            appSettings.controllerHelper(
                {
                    command = VcblCommand.FINISH
                    wsSession = this@with
                },
                { },
                clWsV1,
                "wsV1-finish"
            )
            sessions.remove(this@with)
        }
    }.collect()
}