package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = VcblWordStub.get()

fun validationEnglishCorrect(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
    assertEquals("cat", ctx.wordValidated.english)
}

fun validationEnglishTrim(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = " \n\t cat \t\n ",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
    assertEquals("cat", ctx.wordValidated.english)
}

fun validationEnglishEmpty(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("english", error?.field)
    assertContains(error?.message ?: "", "english")
}

fun validationEnglishSymbols(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "кот",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("english", error?.field)
    assertContains(error?.message ?: "", "english")
}