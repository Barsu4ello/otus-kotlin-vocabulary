package ru.gorbunov.vocabulary.biz.stub

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class WordSearchStubTest {

    private val processor = VcblWordProcessor()
    val filter = VcblWordFilter(searchString = "cat")

    @Test
    fun search() = runTest {

        val ctx = VcblContext(
            command = VcblCommand.SEARCH,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.SUCCESS,
            wordFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.wordsResponse.size > 1)
        val first = ctx.wordsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.english.contains(filter.searchString))
        assertEquals(first.partOfSpeech, VcblPartOfSpeech.NOUN)
    }

    @Test
    fun badId() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.SEARCH,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_ID,
            wordFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.SEARCH,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.DB_ERROR,
            wordFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = VcblContext(
            command = VcblCommand.SEARCH,
            state = VcblState.NONE,
            workMode = VcblWorkMode.STUB,
            stubCase = VcblStubs.BAD_ENGLISH,
            wordFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(VcblWord(), ctx.wordResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}