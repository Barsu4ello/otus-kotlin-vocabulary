package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.biz.addTestPrincipal
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.common.models.VcblWorkMode
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = VcblWordStub.get()

fun validationRussianCorrect(command: VcblCommand, processor: VcblWordProcessor) = runTest {
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
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
    assertEquals("кот", ctx.wordValidated.russian)
}

fun validationRussianTrim(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "cat",
            russian = " \n\t кот \t\n ",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
    assertEquals("кот", ctx.wordValidated.russian)
}

fun validationRussianEmpty(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "cat",
            russian = "",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("russian", error?.field)
    assertContains(error?.message ?: "", "russian")
}

fun validationRussianSymbols(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = stub.id,
            english = "cat",
            russian = "cat",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("russian", error?.field)
    assertContains(error?.message ?: "", "russian")
}