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

class BizRepoReadTest {

    private val userId = VcblWordStubCat.WORD_CAT.ownerId
    private val command = VcblCommand.READ
    private val initWord = VcblWord(
        id = VcblWordId("123"),
        english = "dog",
        russian = "собака",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = userId,
    )
    private val repo = WordRepositoryMock(
        invokeReadWord = {
            DbWordResponseOk(
                data = initWord,
            )
        }
    )
    private val settings = VcblCorSettings(repoTest = repo)
    private val processor = VcblWordProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = VcblContext(
            command = command,
            state = VcblState.NONE,
            workMode = VcblWorkMode.TEST,
            wordRequest = VcblWord(
                id = VcblWordId("123"),
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(VcblState.FINISHING, ctx.state)
        assertEquals(initWord.id, ctx.wordResponse.id)
        assertEquals(initWord.english, ctx.wordResponse.english)
        assertEquals(initWord.russian, ctx.wordResponse.russian)
        assertEquals(initWord.partOfSpeech, ctx.wordResponse.partOfSpeech)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}