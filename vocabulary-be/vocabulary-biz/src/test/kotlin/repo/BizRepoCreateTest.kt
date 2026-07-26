package ru.gorbunov.vocabulary.biz.repo

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.backend.repo.tests.WordRepositoryMock
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.biz.addTestPrincipal
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = VcblUserId("321")
    private val command = VcblCommand.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = WordRepositoryMock(
        invokeCreateWord = {
            DbWordResponseOk(
                data = VcblWord(
                    id = VcblWordId(uuid),
                    english = it.word.english,
                    russian = it.word.russian,
                    partOfSpeech = it.word.partOfSpeech,
                    ownerId = userId,
                )
            )
        }
    )
    private val settings = VcblCorSettings(
        repoTest = repo
    )
    private val processor = VcblWordProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = VcblContext(
            command = command,
            state = VcblState.NONE,
            workMode = VcblWorkMode.TEST,
            wordRequest = VcblWord(
                english = "cat",
                russian = "кот",
                partOfSpeech = VcblPartOfSpeech.NOUN,
            ),
        )
        ctx.addTestPrincipal()
        processor.exec(ctx)
        assertEquals(VcblState.FINISHING, ctx.state)
        assertNotEquals(VcblWordId.NONE, ctx.wordResponse.id)
        assertEquals("cat", ctx.wordResponse.english)
        assertEquals("кот", ctx.wordResponse.russian)
        assertEquals( VcblPartOfSpeech.NOUN, ctx.wordResponse.partOfSpeech)
    }
}