package ru.gorbunov.vocabulary.app.ktor.auth

import apiV1Mapper
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.moduleJvm
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.repo.inmemory.WordRepoInMemory
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiV1Mapper.serializationConfig)
                    setConfig(apiV1Mapper.deserializationConfig)
                }
            }
        }
        application { moduleJvm(VcblAppSettings(corSettings = VcblCorSettings(repoTest = WordRepoInMemory()))) }
        val response = client.post("/v1/word/create") {
            addAuth(groups = emptyList())
            contentType(ContentType.Application.Json)
            setBody(
                WordCreateRequest(
                    word = WordCreateObject(
                        english = "cat",
                        russian = "кот",
                        partOfSpeech = PartOfSpeech.NOUN,
                    ),
                    debug = WordDebug(mode = WordRequestDebugMode.TEST)
                )
            )
        }
        val wordObj = response.body<WordCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(ResponseResult.ERROR, wordObj.result)
        assertEquals("access-create", wordObj.errors?.first()?.code)
    }
}