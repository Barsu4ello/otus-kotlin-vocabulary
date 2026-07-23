package ru.gorbunov.vocabulary.biz.repo
import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.backend.repo.tests.WordRepositoryMock
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseOk
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = VcblUserId("321")
    private val command = VcblCommand.SEARCH
    private val initWord = VcblWord(
        id = VcblWordId("123"),
        english = "dog",
        russian = "собака",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = userId,
    )
    private val repo = WordRepositoryMock(
        invokeSearchWord = {
            DbWordsResponseOk(
                data = listOf(initWord),
            )
        }
    )
    private val settings = VcblCorSettings(repoTest = repo)
    private val processor = VcblWordProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = VcblContext(
            command = command,
            state = VcblState.NONE,
            workMode = VcblWorkMode.TEST,
            wordFilterRequest = VcblWordFilter(
                searchString = "dog",
                partOfSpeech = VcblPartOfSpeech.NOUN
            ),
        )
        processor.exec(ctx)
        assertEquals(VcblState.FINISHING, ctx.state)
        assertEquals(1, ctx.wordsResponse.size)
    }
}