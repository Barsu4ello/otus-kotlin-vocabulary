package ru.gorbunov.vocabulary.e2e.be.base.client

import apiV1Mapper
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.gorbunov.vocabulary.e2e.be.base.AbstractDockerCompose
import ru.gorbunov.vocabulary.e2e.be.base.DockerCompose

/**
 * Отправка запросов по http/rest
 */
class RestAuthClient(dockerCompose: DockerCompose) : Client {
    private val log = org.slf4j.LoggerFactory.getLogger(AbstractDockerCompose::class.java)
//    private val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private val mapper = apiV1Mapper
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp)
    private val token by lazy { runBlocking { getToken() } }

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            path("$version/$path")
        }.build()

        val resp = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                append(HttpHeaders.Authorization, token)
            }
            accept(ContentType.Application.Json)
            setBody(request)

        }.call

        return resp.body()
    }

    private suspend fun getToken(): String {
        val url = urlBuilder.apply {
            path("/realms/vocabulary/protocol/openid-connect/token")
        }.build()
        val response = client.post(url) {
            setBody(
                FormDataContent(Parameters.build {
                    append("client_id", "vocabulary-service")
                    append("grant_type", "password")
                    append("username", "test-user")
                    append("password", "otus")
                })
            )
        }
//        val tokenObj: TokenApi = Json.decodeFromString(response.body())
        println(response.body<String>())
        val tokenObj = mapper.readValue<TokenApi>(response.body<String>())
        log.info("TOKEN: ${tokenObj.accessToken}")
        return "${tokenObj.tokenType} ${tokenObj.accessToken}"
    }

    data class TokenApi(
        @JsonProperty("access_token")
        val accessToken: String = "",
        @JsonProperty("expires_in")
        val expiresIn: Int = 0,
        @JsonProperty("refresh_expires_in")
        val refreshExpiresIn: Int = 0,
        @JsonProperty("refresh_token")
        val refreshToken: String = "",
        @JsonProperty("token_type")
        val tokenType: String = "",
        @JsonProperty("not-before-policy")
        val notBeforePolicy: Int = 0,
        @JsonProperty("session_state")
        val sessionState: String = "",
        @JsonProperty("scope")
        val scope: String = "",
    )
}