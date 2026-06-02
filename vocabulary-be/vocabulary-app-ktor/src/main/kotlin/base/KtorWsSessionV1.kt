package ru.gorbunov.vocabulary.app.ktor.base

import apiV1ResponseSerialize
import io.ktor.websocket.*
import ru.gorbunov.vocabulary.api.v1.models.IResponse
import ru.gorbunov.vocabulary.common.ws.IVcblWsSession

data class KtorWsSessionV1(
    private val session: WebSocketSession
) : IVcblWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        session.send(Frame.Text(apiV1ResponseSerialize(obj)))
    }
}