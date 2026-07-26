package ru.gorbunov.vocabulary.biz.validation

import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.biz.addTestPrincipal
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateEnglishHasContentTest {

    @Test
    fun emptyString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(english = ""))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun noContent() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(english = "12!@#$%^&*()_+-="))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-english-noContent", ctx.errors.first().code)
    }

    @Test
    fun notEnglishString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(english = "кот"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(VcblState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-english-noContent", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = VcblContext(state = VcblState.RUNNING, wordValidating = VcblWord(english = "cat"))
        ctx.addTestPrincipal()
        chain.exec(ctx)
        assertEquals(VcblState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    companion object {
        val chain = rootChain {
            validateEnglishHasContent("")
        }.build()
    }
}