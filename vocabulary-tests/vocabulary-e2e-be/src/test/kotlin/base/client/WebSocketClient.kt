package ru.gorbunov.vocabulary.e2e.be.base.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups
import ru.gorbunov.vocabulary.e2e.be.auth.addAuth
import ru.gorbunov.vocabulary.e2e.be.base.DockerCompose

/**
 * Отправка запросов по http/websocket
 */
class WebSocketClient(dockerCompose: DockerCompose) : Client {
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            protocol = URLProtocol.WS
            path("$version/ws")
        }.build().toString()

        var response = ""
        client.webSocket(
            urlString = url,
            request = {
                addAuth(groups = listOf(VcblUserGroups.USER))
            }
        ) {
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val data = incame.readText()
                // init - игнорим
            }
            send(Frame.Text(request))

            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                response = incame.readText()
            }
        }

        return response
    }
}