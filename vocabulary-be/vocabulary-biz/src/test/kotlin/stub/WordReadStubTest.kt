package ru.gorbunov.vocabulary.biz.stub

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.Test
import kotlin.test.assertEquals

class WordReadStubTest {

    private val processor = VcblWordProcessor()
    val id = VcblWordId("111")

    @Test
    fun read() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.READ,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.SUCCESS,
            wordRequest = VcblWord(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (VcblWordStub.get()) {
            assertEquals(id, ctx.wordResponse.id)
            assertEquals(english, ctx.wordResponse.english)
            assertEquals(russian, ctx.wordResponse.russian)
            assertEquals(partOfSpeech, ctx.wordResponse.partOfSpeech)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.READ,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_ID,
            wordRequest = VcblWord(),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.READ,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.DB_ERROR,
            wordRequest = VcblWord(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.READ,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_ENGLISH,
            wordRequest = VcblWord(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}