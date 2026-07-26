package ru.gorbunov.vocabulary.e2e.be.auth

import apiV1Mapper
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.path
import org.slf4j.LoggerFactory

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

private val mapper = apiV1Mapper
private val log = LoggerFactory.getLogger(TokenApi::class.java)

suspend fun getToken(client: HttpClient, urlBuilder: URLBuilder): String {
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
    println(response.body<String>())
    val tokenObj = mapper.readValue<TokenApi>(response.body<String>())
    log.info("TOKEN: ${tokenObj.accessToken}")
    return "${tokenObj.tokenType} ${tokenObj.accessToken}"
}

