import models.*
import org.junit.Test
import kotlin.test.assertEquals
import ru.gorbunov.vocabulary.api.v1.models.*
import stubs.VcblStubs

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

        assertEquals(req.word, VcblWordStub.get().toTransportWord())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("english", req.errors?.firstOrNull()?.field)
        assertEquals("wrong english val", req.errors?.firstOrNull()?.message)
    }
}