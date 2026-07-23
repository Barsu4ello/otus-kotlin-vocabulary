package ru.gorbunov.vocabulary.biz.repo

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.backend.repo.tests.WordRepositoryMock
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initWord = VcblWord(
    id = VcblWordId("123"),
    english = "cat",
    russian = "кот",
    partOfSpeech = VcblPartOfSpeech.NOUN,
)

private val repo = WordRepositoryMock(
    invokeReadWord = {
        if (it.id == initWord.id) {
            DbWordResponseOk(
                data = initWord,
            )
        } else errorNotFound(it.id)
    }
)
private val settings = VcblCorSettings(repoTest = repo)
private val processor = VcblWordProcessor(settings)

fun repoNotFoundTest(command: VcblCommand) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = VcblWordId("12345"),
            english = "dog",
            russian = "собака",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(VcblState.FAILING, ctx.state)
    assertEquals(VcblWord(), ctx.wordResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}