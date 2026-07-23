package ru.gorbunov.vocabulary.app.ktor.stubs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.moduleJvm
import ru.gorbunov.vocabulary.common.VcblCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class V1WordStubApiTest {
    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = WordCreateRequest(
            word = WordCreateObject(
                english = "cat",
                russian = "кот",
                partOfSpeech = PartOfSpeech.VERB,
            ),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<WordCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.word?.id)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = WordReadRequest(
            word = WordReadObject("111"),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<WordReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.word?.id)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = WordUpdateRequest(
            word = WordUpdateObject(
                id = "111",
                english = "cat",
                russian = "кот",
                partOfSpeech = PartOfSpeech.VERB,
            ),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<WordUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.word?.id)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = WordDeleteRequest(
            word = WordDeleteObject(
                id = "111",
            ),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<WordDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals("111", responseObj.word?.id)
    }

    @Test
    fun search() = v1TestApplication(
        func = "search",
        request = WordSearchRequest(
            wordFilter = WordSearchFilter(),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<WordSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("cat-01", responseObj.words?.first()?.id)
    }



    private fun v1TestApplication(
        func: String,
        request: IRequest,
        function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(VcblAppSettings(corSettings = VcblCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                    enable(SerializationFeature.INDENT_OUTPUT)
                    writerWithDefaultPrettyPrinter()
                }
            }
        }
        val response = client.post("/v1/word/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}