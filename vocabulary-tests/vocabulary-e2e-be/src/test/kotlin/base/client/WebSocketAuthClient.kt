package ru.gorbunov.vocabulary.e2e.be.base.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ru.gorbunov.vocabulary.e2e.be.auth.getToken
import ru.gorbunov.vocabulary.e2e.be.base.DockerCompose

/**
 * Отправка запросов по http/websocket
 */
class WebSocketAuthClient(dockerCompose: DockerCompose) : Client {
    private val httpClient = HttpClient(OkHttp)
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val token by lazy { runBlocking { getToken(httpClient, urlBuilder) } }
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
                header("Authorization", token)
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