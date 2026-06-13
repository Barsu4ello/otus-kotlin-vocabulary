package ru.gorbunov.vocabulary.biz.stub

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class WordUpdateStubTest {

    private val processor = VcblWordProcessor()
    val id = VcblWordId("777")
    val english = "dog"
    val russian = "собака"
    val partOfSpeech = VcblPartOfSpeech.NOUN

    @Test
    fun update() = runTest {

        val ctx = VcblContext(
            command = VcblCommand.UPDATE,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.SUCCESS,
            wordRequest = VcblWord(
                id = id,
                english = english,
                russian = russian,
                partOfSpeech = partOfSpeech,
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.wordResponse.id)
        assertEquals(english, ctx.wordResponse.english)
        assertEquals(russian, ctx.wordResponse.russian)
        assertEquals(partOfSpeech, ctx.wordResponse.partOfSpeech)
    }

    @Test
    fun badId() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.UPDATE,
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
    fun badEnglish() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.CREATE,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_ENGLISH,
            wordRequest = VcblWord(
                id = id,
                english = "собака 666",
                russian = russian,
                partOfSpeech = partOfSpeech,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("english", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badRussian() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.CREATE,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_RUSSIAN,
            wordRequest = VcblWord(
                id = id,
                english = english,
                russian = "dog 666",
                partOfSpeech = partOfSpeech,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("russian", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badPartOfSpeech() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.CREATE,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_PATH_OF_SPEECH,
            wordRequest = VcblWord(
                id = id,
                english = english,
                russian = russian,
                partOfSpeech = VcblPartOfSpeech.NOUN,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("partOfSpeech", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.UPDATE,
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
            command = VcblCommand.UPDATE,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_SEARCH_STRING,
            wordRequest = VcblWord(
                id = id,
                english = english,
                russian = russian,
                partOfSpeech = VcblPartOfSpeech.NOUN,
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}