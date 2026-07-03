package ru.gorbunov.vocabulary.biz.repo

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.backend.repo.tests.WordRepositoryMock
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val command = VcblCommand.DELETE
    private val initWord = VcblWord(
        id = VcblWordId("123"),
        english = "cat",
        russian = "кот",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        lock = VcblWordLock("123-234-abc-ABC"),
    )
    private val repo = WordRepositoryMock(
        invokeReadWord = {
            DbWordResponseOk(
                data = initWord,
            )
        },
        invokeDeleteWord = {
            if (it.id == initWord.id)
                DbWordResponseOk(
                    data = initWord
                )
            else DbWordResponseErr()
        }
    )
    private val settings by lazy {
        VcblCorSettings(
            repoTest = repo
        )
    }
    private val processor = VcblWordProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val wordToDelete = VcblWord(
            id = VcblWordId("123"),
            lock = VcblWordLock("123-234-abc-ABC"),
        )
        val ctx = VcblContext(
            command = command,
            state = VcblState.NONE,
            workMode = VcblWorkMode.TEST,
            wordRequest = wordToDelete,
        )
        processor.exec(ctx)
        assertEquals(VcblState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initWord.id, ctx.wordResponse.id)
        assertEquals(initWord.english, ctx.wordResponse.english)
        assertEquals(initWord.russian, ctx.wordResponse.russian)
        assertEquals(initWord.partOfSpeech, ctx.wordResponse.partOfSpeech)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}