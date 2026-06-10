package ru.gorbunov.vocabulary.app.ktor.websocket

import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.moduleJvm
import ru.gorbunov.vocabulary.common.VcblCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

class V1WebsocketStubTest {

    @Test
    fun createStub() {
        val request = WordCreateRequest(
            word = WordCreateObject(
                english = "cat",
                russian = "кот",
                partOfSpeech = PartOfSpeech.VERB,
            ),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun readStub() {
        val request = WordReadRequest(
            word = WordReadObject("111"),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun updateStub() {
        val request = WordUpdateRequest(
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
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun deleteStub() {
        val request = WordDeleteRequest(
            word = WordDeleteObject(
                id = "111",
            ),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }

    @Test
    fun searchStub() {
        val request = WordSearchRequest(
            wordFilter = WordSearchFilter(),
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS
            )
        )

        testMethod<IResponse>(request) {
            assertEquals(ResponseResult.SUCCESS, it.result)
        }
    }



    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit
    ) = testApplication {
        application { moduleJvm(VcblAppSettings(corSettings = VcblCorSettings())) }
        val client = createClient {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter()
            }
        }

        client.webSocket("/v1/ws") {
            withTimeout(3.seconds) {
                val response = receiveDeserialized<IResponse>() as T
                assertIs<WordInitResponse>(response)
            }
            sendSerialized(request)
            withTimeout(3.seconds) {
                val response = receiveDeserialized<IResponse>() as T
                assertBlock(response)
            }
        }
    }
}