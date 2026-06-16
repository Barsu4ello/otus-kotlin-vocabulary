package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidatePartOfSpeechTest {

    @Test
    fun partOfSpeechIsNone() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(partOfSpeech = VcblPartOfSpeech.NONE))
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-partOfSpeech-badFormat", ctx.errors.first().code)
    }



    @Test
    fun normalPartOfSpeech() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(partOfSpeech = VcblPartOfSpeech.VERB))
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validatePartOfSpeech("")
        }.build()
    }
}