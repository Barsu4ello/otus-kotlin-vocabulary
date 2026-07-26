package ru.gorbunov.vocabulary.biz.repo

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.backend.repo.tests.WordRepositoryMock
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.biz.addTestPrincipal
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.stubs.VcblWordStubCat
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = VcblWordStubCat.WORD_CAT.ownerId
    private val command = VcblCommand.UPDATE
    private val initWord = VcblWord(
        id = VcblWordId("123"),
        english = "cat",
        russian = "кот",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = userId,
        lock = VcblWordLock("123-234-abc-ABC"),
    )
    private val repo = WordRepositoryMock(
        invokeReadWord = {
            DbWordResponseOk(
                data = initWord,
            )
        },
        invokeUpdateWord = {
            DbWordResponseOk(
                data = VcblWord(
                    id = VcblWordId("123"),
                    english = "cat",
                    russian = "кошка",
                    partOfSpeech = VcblPartOfSpeech.NOUN,
                    lock = VcblWordLock("123-234-abc-ABC"),
                )
            )
        }
    )
    private val settings = VcblCorSettings(repoTest = repo)
    private val processor = VcblWordProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val wordToUpdate = VcblWord(
            id = VcblWordId("123"),
            english = "cat",
            russian = "кошка",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        )
        val ctx = VcblContext(
            command = command,
            state = VcblState.NONE,
            workMode = VcblWorkMode.TEST,
            wordRequest = wordToUpdate,
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(VcblState.FINISHING, ctx.state)
        assertEquals(wordToUpdate.id, ctx.wordResponse.id)
        assertEquals(wordToUpdate.english, ctx.wordResponse.english)
        assertEquals(wordToUpdate.russian, ctx.wordResponse.russian)
        assertEquals(wordToUpdate.partOfSpeech, ctx.wordResponse.partOfSpeech)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}