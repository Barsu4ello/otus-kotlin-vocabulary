package ru.gorbunov.vocabulary.app.common

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.api.kmp.v1.mapper.fromTransport
import ru.gorbunov.vocabulary.api.kmp.v1.mapper.toTransportWord
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = WordCreateRequest(
        word = WordCreateObject(
            english = "cat",
            russian = "кот",
            partOfSpeech = PartOfSpeech.VERB,
        ),
        debug = WordDebug(mode = WordRequestDebugMode.STUB, stub = WordRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IVcblAppSettings = object : IVcblAppSettings {
        override val corSettings: VcblCorSettings = VcblCorSettings()
        override val processor: VcblWordProcessor = VcblWordProcessor(corSettings)
    }

    private suspend fun createWordSpring(request: WordCreateRequest): WordCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportWord() as WordCreateResponse },
            ControllerV2Test::class,
            "controller-v1-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createWordKtor(appSettings: IVcblAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<WordCreateRequest>()) },
            { toTransportWord() },
            ControllerV2Test::class,
            "controller-v1-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createWordSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createWordKtor(appSettings) }
        val res = testApp.res as WordCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}