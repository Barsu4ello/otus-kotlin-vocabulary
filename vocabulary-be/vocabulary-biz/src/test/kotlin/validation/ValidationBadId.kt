package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.biz.addTestPrincipal
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.common.models.VcblWorkMode
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWordStub.get(),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
}

fun validationIdTrim(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWordStub.prepareResult {
            id = VcblWordId(" \n\t ${id.asString()} \n\t ")
        },
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(VcblState.FAILING, ctx.state)
}

fun validationIdEmpty(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = VcblWordId(""),
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: VcblCommand, processor: VcblWordProcessor) = runTest {
    val ctx = VcblContext(
        command = command,
        state = VcblState.NONE,
        workMode = VcblWorkMode.TEST,
        wordRequest = VcblWord(
            id = VcblWordId("!@#\$%^&*(),.{}"),
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            lock = VcblWordLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal()
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(VcblState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}