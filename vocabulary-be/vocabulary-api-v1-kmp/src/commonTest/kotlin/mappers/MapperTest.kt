package mappers

import ru.gorbunov.vocabulary.api.kmp.v1.mapper.fromTransport
import ru.gorbunov.vocabulary.api.kmp.v1.mapper.toTransportCreateWord
import ru.gorbunov.vocabulary.api.kmp.v1.mapper.toTransportWord
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val req = WordCreateRequest(
            debug = WordDebug(
                mode = WordRequestDebugMode.STUB,
                stub = WordRequestDebugStubs.SUCCESS,
            ),
            word = VcblWordStub.get().toTransportCreateWord()
        )

        val expected = VcblWordStub.prepareResult {
            id = VcblWordId.NONE
            ownerId = VcblUserId.NONE
            lock = VcblWordLock.NONE
        }


        val context = VcblContext()
        context.fromTransport(req)

        assertEquals(VcblStubs.SUCCESS, context.stubCase)
        assertEquals(VcblWorkMode.STUB, context.workMode)
        assertEquals(expected, context.wordRequest)
    }

    @Test
    fun toTransport() {
        val context = VcblContext(
            requestId = VcblRequestId("1234"),
            command = VcblCommand.CREATE,
            wordResponse = VcblWordStub.get(),
            errors = mutableListOf(
                VcblError(
                    code = "err",
                    group = "request",
                    field = "english",
                    message = "wrong english val",
                )
            ),
            state = VcblState.RUNNING,
        )

        val req = context.toTransportWord() as WordCreateResponse

        assertEquals(VcblWordStub.get().toTransportWord(), req.word)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("english", req.errors?.firstOrNull()?.field)
        assertEquals("wrong english val", req.errors?.firstOrNull()?.message)
    }
}